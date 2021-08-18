package com.sms.courier.repository.impl;

import static com.sms.courier.common.enums.OperationModule.API;
import static com.sms.courier.common.enums.OperationModule.API_GROUP;
import static com.sms.courier.common.enums.OperationModule.API_TAG;
import static com.sms.courier.common.enums.OperationModule.API_TEST_CASE;
import static com.sms.courier.common.field.ApiField.API_NAME;
import static com.sms.courier.common.field.ApiField.API_PATH;
import static com.sms.courier.common.field.ApiField.API_PROTOCOL;
import static com.sms.courier.common.field.ApiField.API_STATUS;
import static com.sms.courier.common.field.ApiField.GROUP_ID;
import static com.sms.courier.common.field.ApiField.REQUEST_METHOD;
import static com.sms.courier.common.field.ApiField.TAG_ID;
import static com.sms.courier.common.field.ApiTag.GROUP_NAME;
import static com.sms.courier.common.field.ApiTag.TAG_NAME;
import static com.sms.courier.common.field.ApiTestCaseField.CASE_API_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;

import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.entity.mongo.GroupResultVo;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.ApiGroupRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
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
        return commonRepository.findById(id, API.getCollectionName(), getLookupVo(), ApiResponse.class);
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        QueryVo queryVo =
            QueryVo.builder().collectionName(API.getCollectionName()).criteriaList(getCriteriaList(apiPageRequest))
                .build();
        List<LookupVo> lookupVo = getLookupVo();
        queryVo.setLookupVo(lookupVo);
        Page<ApiResponse> page = commonRepository.page(queryVo, apiPageRequest, ApiResponse.class);
        queryApiTestCase(page.getContent());
        return page;
    }

    private void queryApiTestCase(List<ApiResponse> apiResponses) {
        if (apiResponses.isEmpty()) {
            return;
        }
        List<ObjectId> ids = apiResponses.stream().map(ApiResponse::getId).map(ObjectId::new)
            .collect(Collectors.toList());
        GroupOperation groupBy = Aggregation.group(CASE_API_ID.getName()).count().as("count");
        MatchOperation match = Aggregation.match(Criteria.where(CASE_API_ID.getName()).in(ids));
        List<GroupResultVo> results = mongoTemplate
            .aggregate(Aggregation.newAggregation(match, groupBy), API_TEST_CASE.getCollectionName(),
                GroupResultVo.class).getMappedResults();
        Map<String, Integer> map = results.stream()
            .collect(Collectors.toUnmodifiableMap(GroupResultVo::getId, GroupResultVo::getCount));
        apiResponses.forEach(apiResponse -> apiResponse.setCaseCount(map.getOrDefault(apiResponse.getId(), 0)));
    }

    private List<LookupVo> getLookupVo() {
        List<LookupField> tagField = List.of(LookupField.builder().field(TAG_NAME).build());
        List<LookupField> groupField =
            List.of(LookupField.builder().field(GROUP_NAME).alias("groupName").build());
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

    @Override
    public Boolean updateFieldByIds(List<String> ids, UpdateRequest<Object> updateRequest) {
        return commonRepository.updateFieldByIds(ids, updateRequest, ApiEntity.class);
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
