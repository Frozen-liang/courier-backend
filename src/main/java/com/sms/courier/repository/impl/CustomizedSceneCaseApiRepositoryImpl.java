package com.sms.courier.repository.impl;

import com.google.common.collect.Lists;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseApiRepositoryImpl implements CustomizedSceneCaseApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;

    public CustomizedSceneCaseApiRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonDeleteRepository;
    }

    @Override
    public List<SceneCaseApiEntity> findSceneCaseApiByApiIds(List<String> ids) {
        Query query = new Query();
        SceneField.API_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class);
    }

    @Override
    public Boolean deleteSceneCaseApiConn(List<String> caseTemplateApiId) {
        for (String id : caseTemplateApiId) {
            Query query = new Query();
            CaseTemplateApiConn build = CaseTemplateApiConn.builder().caseTemplateApiId(id)
                .execute(Boolean.TRUE).build();
            query.addCriteria(Criteria.where(SceneField.CASE_TEMPLATE_API_CONN_LIST.getName()).is(build));
            Update update = new Update();
            update.pullAll(SceneField.CASE_TEMPLATE_API_CONN_LIST.getName(), Lists.newArrayList(build).toArray());

            mongoTemplate.updateMulti(query, update, SceneCaseApiEntity.class);
            CaseTemplateApiConn buildFalse = CaseTemplateApiConn.builder().caseTemplateApiId(id)
                .execute(Boolean.FALSE).build();
            update.pullAll(SceneField.CASE_TEMPLATE_API_CONN_LIST.getName(), Lists.newArrayList(buildFalse).toArray());
            mongoTemplate.updateMulti(query, update, SceneCaseApiEntity.class);
        }
        return Boolean.TRUE;
    }

    @Override
    public List<String> findSceneCaseApiIdsBySceneCaseIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(CommonField.ID.getName());
        SceneField.SCENE_CASE_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class).stream().map(SceneCaseApiEntity::getId).filter(
            Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteByIds(List<String> sceneCaseApiIds) {
        return commonRepository.deleteByIds(sceneCaseApiIds, SceneCaseApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> sceneCaseApiIds) {
        return commonRepository.recover(sceneCaseApiIds, SceneCaseApiEntity.class);
    }

    @Override
    public long findCountByCaseTemplateIdAndNowProjectId(ObjectId caseTemplateId, ObjectId projectId,
        boolean isNowObject) {
        Query query = new Query();
        SceneField.CASE_TEMPLATE_ID.is(caseTemplateId).ifPresent(query::addCriteria);
        if (isNowObject) {
            CommonField.PROJECT_ID.is(projectId).ifPresent(query::addCriteria);
        } else {
            CommonField.PROJECT_ID.ne(projectId).ifPresent(query::addCriteria);
        }
        CommonField.REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        return mongoTemplate.count(query, "SceneCaseApi");
    }

    @Override
    public List<String> findSceneCaseIdByCaseTemplateIds(List<String> caseTemplateIds) {
        Query query = new Query();
        query.fields().include(SceneField.SCENE_CASE_ID.getName());
        SceneField.CASE_TEMPLATE_ID.in(caseTemplateIds).ifPresent(query::addCriteria);
        CommonField.REMOVE.is(Boolean.FALSE).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class).stream()
            .map(SceneCaseApiEntity::getSceneCaseId)
            .distinct()
            .collect(Collectors.toList());
    }

}
