package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.CollectionName.API_HISTORY;
import static com.sms.courier.common.field.ApiHistoryField.API_ID;
import static com.sms.courier.common.field.ApiTagField.GROUP_NAME;
import static com.sms.courier.common.field.ApiTagField.TAG_NAME;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.UserField.NICKNAME;
import static com.sms.courier.common.field.UserField.USERNAME;

import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.field.ApiHistoryField;
import com.sms.courier.dto.response.ApiHistoryDetailResponse;
import com.sms.courier.dto.response.ApiHistoryListResponse;
import com.sms.courier.dto.response.ApiHistoryResponse;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.ApiHistoryService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApiHistoryServiceImpl implements ApiHistoryService {

    private final CommonRepository commonRepository;

    public ApiHistoryServiceImpl(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public List<ApiHistoryListResponse> findByApiId(ObjectId apiId) {
        return commonRepository.listLookupUser(API_HISTORY.getName(), List.of(API_ID.is(apiId)),
            ApiHistoryListResponse.class);
    }

    @Override
    public ApiHistoryDetailResponse findById(String id) {
        return commonRepository.findById(id, API_HISTORY.getName(), getLookupVo(), ApiHistoryResponse.class)
            .map(this::toApiHistoryDetailResponse).orElse(null);
    }

    private List<LookupVo> getLookupVo() {
        List<LookupVo> lookupVoList = new ArrayList<>();

        List<LookupField> managerUserField = List.of(
            LookupField.builder().field(USERNAME).alias("apiManager").build()
        );
        lookupVoList.add(
            LookupVo.builder().from(CollectionName.USER).localField(ApiHistoryField.API_MANAGER_ID).foreignField(ID)
                .as("manager").queryFields(managerUserField).build());

        List<LookupField> tagField = List.of(LookupField.builder().field(TAG_NAME).build());
        lookupVoList
            .add(LookupVo.builder().from(CollectionName.API_TAG).localField(ApiHistoryField.TAG_ID).foreignField(ID)
                .queryFields(tagField)
                .as("apiTag").build());

        List<LookupField> createUserField = List.of(
            LookupField.builder().field(USERNAME).alias("createUsername").build(),
            LookupField.builder().field(NICKNAME).alias("createNickname").build()
        );
        List<LookupField> groupField =
            List.of(LookupField.builder().field(GROUP_NAME).alias("groupName").build());

        lookupVoList
            .add(LookupVo.builder().from(CollectionName.API_GROUP).localField(ApiHistoryField.GROUP_ID).foreignField(ID)
                .queryFields(groupField)
                .as("apiGroup").build());
        lookupVoList.add(
            LookupVo.builder().from(CollectionName.USER).localField(ApiHistoryField.CREATE_USER_ID).foreignField(ID).as(
                "createUser")
                .queryFields(createUserField).build());
        return lookupVoList;
    }

    private ApiHistoryDetailResponse toApiHistoryDetailResponse(ApiHistoryResponse apiHistoryResponse) {
        ApiHistoryDetailResponse apiHistoryDetailResponse = apiHistoryResponse.getRecord();
        apiHistoryDetailResponse.setApiManager(apiHistoryResponse.getApiManager());
        apiHistoryDetailResponse.setGroupName(apiHistoryResponse.getGroupName());
        apiHistoryDetailResponse.setCreateUsername(apiHistoryResponse.getCreateUsername());
        apiHistoryDetailResponse.setCreateNickname(apiHistoryResponse.getCreateNickname());
        apiHistoryDetailResponse.setTagName(apiHistoryResponse.getTagName());
        return apiHistoryDetailResponse;
    }

}
