package com.sms.satp.repository.impl;

import com.google.common.collect.Lists;
import com.sms.satp.common.field.CommonField;
import com.sms.satp.common.field.SceneField;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import java.util.List;
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
    public List<SceneCaseApiEntity> findSceneCaseApiIdsBySceneCaseIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(CommonField.ID.getName());
        SceneField.SCENE_CASE_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> sceneCaseApiIds) {
        return commonRepository.deleteByIds(sceneCaseApiIds, SceneCaseApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> sceneCaseApiIds) {
        return commonRepository.recover(sceneCaseApiIds, SceneCaseApiEntity.class);
    }

}
