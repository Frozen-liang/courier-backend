package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.ApiTagField.GROUP_NAME;
import static com.sms.courier.common.field.ApiTagField.TAG_NAME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static com.sms.courier.common.field.SceneField.GROUP_ID;
import static com.sms.courier.common.field.SceneField.NAME;
import static com.sms.courier.common.field.SceneField.PRIORITY;
import static com.sms.courier.common.field.SceneField.REVIEW_STATUS;
import static com.sms.courier.common.field.SceneField.TAG_ID;
import static com.sms.courier.common.field.SceneField.TEST_STATUS;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.entity.group.SceneCaseGroupEntity;
import com.sms.courier.entity.mongo.LookupField;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.repository.SceneCaseGroupRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    private final SceneCaseGroupRepository sceneCaseGroupRepository;

    public CustomizedSceneCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository, SceneCaseGroupRepository sceneCaseGroupRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
        this.sceneCaseGroupRepository = sceneCaseGroupRepository;
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

    @Override
    public Optional<SceneCaseResponse> findById(String id) {
        List<LookupVo> lookupVoList = getLookupVoList();
        return commonRepository.findById(id, "SceneCase", lookupVoList, SceneCaseResponse.class);
    }

    @Override
    public Long count(List<String> projectIds) {
        Query query = new Query();
        REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        PROJECT_ID.in(projectIds).ifPresent(query::addCriteria);
        return mongoTemplate.count(query, SceneCaseEntity.class);
    }

    private List<LookupVo> getLookupVoList() {
        return Lists.newArrayList(
            LookupVo.builder()
                .from(CollectionName.SCENE_CASE_GROUP)
                .localField(GROUP_ID)
                .foreignField(ID)
                .as("sceneCaseGroup")
                .queryFields(Lists.newArrayList(LookupField.builder().field(GROUP_NAME).alias("groupName").build()))
                .build(),
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

    private List<Optional<Criteria>> buildCriteria(SearchSceneCaseRequest searchSceneCaseRequest, ObjectId projectId) {
        return Lists.newArrayList(
            PROJECT_ID.is(projectId),
            REMOVE.is(searchSceneCaseRequest.isRemoved()),
            NAME.like(searchSceneCaseRequest.getName()),
            GROUP_ID.in(getApiGroupId(searchSceneCaseRequest.getGroupId())),
            TEST_STATUS.in(searchSceneCaseRequest.getTestStatus()),
            TAG_ID.in(searchSceneCaseRequest.getTagId()),
            PRIORITY.in(searchSceneCaseRequest.getPriority()),
            CREATE_USER_ID.in(searchSceneCaseRequest.getCreateUserId()),
            REVIEW_STATUS.is(searchSceneCaseRequest.getReviewStatus())
        );
    }

    private List<ObjectId> getApiGroupId(ObjectId groupId) {
        return Optional.ofNullable(groupId).map(ObjectId::toString)
            .map(sceneCaseGroupRepository::findById)
            .map((Optional::get))
            .map(SceneCaseGroupEntity::getRealGroupId)
            .map(sceneCaseGroupRepository::findAllByPathContains)
            .orElse(Stream.empty())
            .map(SceneCaseGroupEntity::getId)
            .map(ObjectId::new)
            .collect(Collectors.toList());
    }
}
