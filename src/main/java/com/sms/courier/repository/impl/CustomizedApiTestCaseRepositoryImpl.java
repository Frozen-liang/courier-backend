package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.ApiTagField.TAG_NAME;
import static com.sms.courier.common.field.ApiTestCaseField.CASE_API_ID;
import static com.sms.courier.common.field.ApiTestCaseField.CASE_NAME;
import static com.sms.courier.common.field.ApiTestCaseField.STATUS;
import static com.sms.courier.common.field.CommonField.API_ID;
import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static com.sms.courier.common.field.SceneField.TAG_ID;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ApiTestCasePageRequest;
import com.sms.courier.dto.response.ApiTestCasePageResponse;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiTestCaseRepositoryImpl implements CustomizedApiTestCaseRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;

    public CustomizedApiTestCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }


    @Override
    public void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status) {
        Query query = new Query();
        API_ID.in(apiIds).ifPresent(query::addCriteria);
        Update update = new Update();
        update.set(STATUS.getName(), status.getCode());
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteById(String id) {
        return commonRepository.deleteById(id, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, ApiTestCaseEntity.class);
    }

    @Override
    public List<ApiTestCaseResponse> listByJoin(ObjectId apiId, ObjectId projectId, boolean removed) {
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("ApiTestCase")
            .lookupVo(lookupVoList)
            .criteriaList(buildCriteria(apiId, projectId, removed))
            .build();
        return commonRepository.list(queryVo, ApiTestCaseResponse.class);
    }

    @Override
    public List<String> findApiIdsByTestIds(List<String> ids) {
        List<ApiTestCaseEntity> entityList = commonRepository.findIncludeFieldByIds(ids, "ApiTestCase",
            Lists.newArrayList(API_ID.getName()), ApiTestCaseEntity.class);
        return entityList.stream().map(entity -> entity.getApiEntity().getId())
            .collect(Collectors.toList());
    }

    @Override
    public Long countByProjectIds(List<String> projectIds, LocalDateTime dateTime) {
        Query query = new Query();
        List<ObjectId> objectIdList = projectIds.stream().map(ObjectId::new).collect(Collectors.toList());
        PROJECT_ID.in(objectIdList).ifPresent(query::addCriteria);
        CREATE_DATE_TIME.gt(dateTime).ifPresent(query::addCriteria);
        REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        return mongoTemplate.count(query, "ApiTestCase");
    }

    @Override
    public Page<ApiTestCaseResponse> getCasePageByProjectIdsAndCreateDate(List<String> projectIds,
        LocalDateTime dateTime, PageDto pageDto) {
        List<ObjectId> objectIdList = projectIds.stream().map(ObjectId::new).collect(Collectors.toList());
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("ApiTestCase")
            .criteriaList(
                Lists.newArrayList(PROJECT_ID.in(objectIdList),
                    CREATE_DATE_TIME.gt(dateTime),
                    REMOVE.is(Boolean.FALSE)))
            .lookupVo(lookupVoList)
            .build();
        return commonRepository.page(queryVo, pageDto, ApiTestCaseResponse.class);
    }

    @Override
    public Page<ApiTestCasePageResponse> page(ApiTestCasePageRequest request) {
        QueryVo query = new QueryVo();
        query.setEntityClass(ApiTestCaseEntity.class);
        query.setLookupVo(List.of(LookupVo.createLookupUser()));
        List<Optional<Criteria>> list = new ArrayList<>();
        list.add(CASE_NAME.like(request.getCaseName()));
        list.add(TAG_ID.in(request.getTagId()));
        list.add(CASE_API_ID.is(request.getApiId()));
        list.add(PROJECT_ID.is(request.getProjectId()));
        list.add(STATUS.is(request.getStatus()));
        query.setCriteriaList(list);
        return commonRepository.page(query, request, ApiTestCasePageResponse.class);
    }

    @Override
    public Long count(List<String> projectIds) {
        Query query = new Query();
        PROJECT_ID.in(projectIds).ifPresent(query::addCriteria);
        REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        return mongoTemplate.count(query, ApiTestCaseEntity.class);
    }

    private List<LookupVo> getLookupVoList() {
        return Lists.newArrayList(
            LookupVo.builder()
                .from(CollectionName.API_TAG)
                .localField(TAG_ID)
                .foreignField(ID)
                .as("apiTag")
                .queryFields(Lists.newArrayList(LookupField.builder().field(TAG_NAME).build()))
                .build(),
            LookupVo.builder()
                .from(CollectionName.USER)
                .localField(CREATE_USER_ID)
                .foreignField(ID)
                .as("user")
                .queryFields(Lists.newArrayList(LookupField.builder().field(USERNAME).alias("createUsername").build()))
                .build()
        );
    }

    private List<Optional<Criteria>> buildCriteria(ObjectId apiId, ObjectId projectId, boolean removed) {
        return Lists.newArrayList(
            PROJECT_ID.is(projectId),
            REMOVE.is(removed),
            API_ID.is(apiId)
        );
    }

}
