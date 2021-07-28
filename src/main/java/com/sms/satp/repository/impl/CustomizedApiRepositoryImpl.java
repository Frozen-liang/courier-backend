package com.sms.satp.repository.impl;

import static com.sms.satp.common.enums.OperationModule.API;
import static com.sms.satp.common.enums.OperationModule.API_GROUP;
import static com.sms.satp.common.enums.OperationModule.API_TAG;
import static com.sms.satp.common.field.ApiField.API_NAME;
import static com.sms.satp.common.field.ApiField.API_PATH;
import static com.sms.satp.common.field.ApiField.API_PROTOCOL;
import static com.sms.satp.common.field.ApiField.API_STATUS;
import static com.sms.satp.common.field.ApiField.GROUP_ID;
import static com.sms.satp.common.field.ApiField.REQUEST_METHOD;
import static com.sms.satp.common.field.ApiField.TAG_ID;
import static com.sms.satp.common.field.ApiTag.GROUP_NAME;
import static com.sms.satp.common.field.ApiTag.TAG_NAME;
import static com.sms.satp.common.field.CommonField.ID;
import static com.sms.satp.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.satp.common.field.CommonField.PROJECT_ID;
import static com.sms.satp.common.field.CommonField.REMOVE;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.entity.mongo.LookupQueryField;
import com.sms.satp.entity.mongo.LookupVo;
import com.sms.satp.entity.mongo.QueryVo;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiRepositoryImpl implements CustomizedApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;
    private final ApiGroupRepository apiGroupRepository;

    public CustomizedApiRepositoryImpl(MongoTemplate mongoTemplate, CommonRepository commonRepository,
        ApiGroupRepository apiGroupRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
        this.apiGroupRepository = apiGroupRepository;
    }


    @Override
    public Optional<ApiResponse> findById(String id) {
        return commonRepository.findById(id, API.getCollectionName(), getLookupVos(), ApiResponse.class);
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        QueryVo queryVo =
            QueryVo.builder().collectionName(API.getCollectionName()).criteriaList(getCriteriaList(apiPageRequest))
                .build();
        List<LookupVo> lo = getLookupVos();
        queryVo.setLookupVos(lo);
        return commonRepository.page(queryVo, apiPageRequest, ApiResponse.class);
    }

    private List<LookupVo> getLookupVos() {
        List<LookupQueryField> tagField = List.of(LookupQueryField.builder().field(TAG_NAME).build());
        List<LookupQueryField> groupField =
            List.of(LookupQueryField.builder().field(GROUP_NAME).alias("groupName").build());
        return List.of(LookupVo.builder().from(API_TAG).localField(TAG_ID).foreignField(ID).queryFields(tagField)
                .as("apiTag").build(),
            LookupVo.builder().from(API_GROUP).localField(GROUP_ID).foreignField(ID).queryFields(groupField)
                .as("apiGroup").build());
    }

    @Override
    public Boolean deleteById(String id) {
        return commonRepository.deleteById(id, ApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, ApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, ApiEntity.class);
    }

    @Override
    public void deleteByGroupIds(List<String> groupIds) {
        Query query = new Query(Criteria.where(GROUP_ID.getName()).in(groupIds));
        Update update = Update.update(REMOVE.getName(), Boolean.TRUE);
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, ApiEntity.class);
    }

    private void createLookUpOperation(List<AggregationOperation> aggregationOperations) {
        LookupOperation apiTagLookupOperation =
            LookupOperation.newLookup().from(API_TAG.getCollectionName()).localField(TAG_ID.getName())
                .foreignField(ID.getName())
                .as("apiTag");
        LookupOperation apiGroupLookupOperation =
            LookupOperation.newLookup().from(API_GROUP.getCollectionName()).localField(GROUP_ID.getName())
                .foreignField(ID.getName())
                .as("apiGroup");
        aggregationOperations.add(apiTagLookupOperation);
        aggregationOperations.add(apiGroupLookupOperation);
    }

    private List<Optional<Criteria>> getCriteriaList(ApiPageRequest apiPageRequest) {
        List<Optional<Criteria>> criteriaList = new ArrayList<>();
        criteriaList.add(REMOVE.is(apiPageRequest.isRemoved()));
        criteriaList.add(PROJECT_ID.is(apiPageRequest.getProjectId()));
        criteriaList.add(API_NAME.like(apiPageRequest.getApiName()));
        criteriaList.add(API_PATH.like(apiPageRequest.getApiPath()));
        criteriaList.add(API_PROTOCOL.in(apiPageRequest.getApiProtocol()));
        criteriaList.add(API_STATUS.in(apiPageRequest.getApiStatus()));
        criteriaList.add(GROUP_ID.in(getApiGroupId(apiPageRequest.getGroupId())));
        criteriaList.add(REQUEST_METHOD.in(apiPageRequest.getRequestMethod()));
        criteriaList.add(TAG_ID.in(apiPageRequest.getTagId()));
        return criteriaList;
    }

    private List<ObjectId> getApiGroupId(ObjectId groupId) {
        return Optional.ofNullable(groupId).map(ObjectId::toString)
            .map(apiGroupRepository::findById)
            .map((Optional::get))
            .map(ApiGroupEntity::getRealGroupId)
            .map(apiGroupRepository::findAllByPathContains)
            .orElse(Stream.empty())
            .map(ApiGroupEntity::getId)
            .map(ObjectId::new)
            .collect(Collectors.toList());
    }
}
