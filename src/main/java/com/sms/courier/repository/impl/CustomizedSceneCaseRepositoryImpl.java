package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.ApiTag.GROUP_NAME;
import static com.sms.courier.common.field.ApiTag.TAG_NAME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static com.sms.courier.common.field.SceneField.CREATE_USER_NAME;
import static com.sms.courier.common.field.SceneField.GROUP_ID;
import static com.sms.courier.common.field.SceneField.NAME;
import static com.sms.courier.common.field.SceneField.PRIORITY;
import static com.sms.courier.common.field.SceneField.TAG_ID;
import static com.sms.courier.common.field.SceneField.TEST_STATUS;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseRepositoryImpl implements CustomizedSceneCaseRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;

    public CustomizedSceneCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }

    @Override
    public Page<SceneCaseResponse> search(SearchSceneCaseRequest searchSceneCaseRequest, ObjectId projectId) {
        List<LookupVo> lookupVoList = getLookupVoList();
        QueryVo queryVo = QueryVo.builder()
            .collectionName("SceneCase")
            .lookupVo(lookupVoList)
            .criteriaList(buildCriteria(searchSceneCaseRequest, projectId))
            .build();
        return commonRepository.page(queryVo, searchSceneCaseRequest, SceneCaseResponse.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, SceneCaseEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, SceneCaseEntity.class);
    }

    @Override
    public List<SceneCaseEntity> getSceneCaseIdsByGroupIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(ID.getName());
        CommonField.GROUP_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseEntity.class);
    }

    @Override
    public Boolean deleteGroupIdByIds(List<String> ids) {
        return commonRepository.deleteFieldByIds(ids, CommonField.GROUP_ID.getName(), SceneCaseEntity.class);
    }

    private List<LookupVo> getLookupVoList() {
        return Lists.newArrayList(
            LookupVo.builder()
                .from(OperationModule.SCENE_CASE_GROUP)
                .localField(GROUP_ID)
                .foreignField(ID)
                .as("sceneCaseGroup")
                .queryFields(Lists.newArrayList(LookupField.builder().field(GROUP_NAME).alias("groupName").build()))
                .build(),
            LookupVo.builder()
                .from(OperationModule.API_TAG)
                .localField(TAG_ID)
                .foreignField(ID)
                .as("apiTag")
                .queryFields(Lists.newArrayList(LookupField.builder().field(TAG_NAME).build()))
                .build(),
            LookupVo.builder()
                .from(OperationModule.USER)
                .localField(CREATE_USER_ID)
                .foreignField(ID)
                .as("user")
                .queryFields(Lists.newArrayList(LookupField.builder().field(USERNAME).alias("createUsername").build()))
                .build()
        );
    }

    private List<Optional<Criteria>> buildCriteria(SearchSceneCaseRequest searchSceneCaseRequest, ObjectId projectId) {
        return Lists.newArrayList(
            PROJECT_ID.is(projectId),
            REMOVE.is(searchSceneCaseRequest.isRemoved()),
            NAME.like(searchSceneCaseRequest.getName()),
            GROUP_ID.is(searchSceneCaseRequest.getGroupId()),
            TEST_STATUS.in(searchSceneCaseRequest.getTestStatus()),
            TAG_ID.in(searchSceneCaseRequest.getTagId()),
            PRIORITY.in(searchSceneCaseRequest.getPriority()),
            CREATE_USER_NAME.in(searchSceneCaseRequest.getCreateUserName())
        );
    }

}
