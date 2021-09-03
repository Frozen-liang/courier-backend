package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.USERNAME;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.field.MockField;
import com.sms.courier.dto.request.MockApiPageRequest;
import com.sms.courier.dto.response.MockApiResponse;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedMockApiRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class CustomizedMockApiRepositoryImpl implements CustomizedMockApiRepository {

    private final CommonRepository commonRepository;

    public CustomizedMockApiRepositoryImpl(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public Page<MockApiResponse> page(ObjectId apiId, MockApiPageRequest pageRequest) {
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("MockApi")
            .lookupVo(lookupVoList)
            .criteriaList(buildCriteria(pageRequest, apiId))
            .build();
        return commonRepository.page(queryVo, pageRequest, MockApiResponse.class);
    }

    @Override
    public List<MockApiResponse> list(ObjectId apiId, Boolean isEnable) {
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("MockApi")
            .lookupVo(lookupVoList)
            .criteriaList(buildListCriteria(isEnable, apiId))
            .build();
        return commonRepository.list(queryVo, MockApiResponse.class);
    }

    private List<LookupVo> getLookupVoList() {
        return Lists.newArrayList(
            LookupVo.builder()
                .from(CollectionName.USER)
                .localField(CREATE_USER_ID)
                .foreignField(ID)
                .as("user")
                .queryFields(Lists.newArrayList(LookupField.builder().field(USERNAME).alias("createUsername").build()))
                .build());
    }

    private List<Optional<Criteria>> buildCriteria(MockApiPageRequest pageRequest, ObjectId apiId) {
        return Lists.newArrayList(
            MockField.API_ID.is(apiId),
            MockField.IS_ENABLE.is(pageRequest.getIsEnable())
        );
    }

    private List<Optional<Criteria>> buildListCriteria(Boolean isEnable, ObjectId apiId) {
        return Lists.newArrayList(
            MockField.API_ID.is(apiId),
            MockField.IS_ENABLE.is(isEnable)
        );
    }

}
