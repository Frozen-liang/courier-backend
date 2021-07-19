package com.sms.satp.repository.impl;

import com.google.common.collect.Lists;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseApiRepositoryImpl implements CustomizedSceneCaseApiRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseApiRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int findCurrentOrderBySceneCaseId(String sceneCaseId) {
        Query query = new Query();
        SceneFiled.SCENE_CASE_ID.is(sceneCaseId).ifPresent(query::addCriteria);
        query.with(Sort.by(Direction.DESC, SceneFiled.ORDER.getFiled()));
        query.limit(1);
        SceneCaseApiEntity sceneCaseApi = mongoTemplate.findOne(query, SceneCaseApiEntity.class);
        return Objects.isNull(sceneCaseApi) ? 1 : sceneCaseApi.getOrder() + 1;
    }

    @Override
    public List<SceneCaseApiEntity> findSceneCaseApiByApiIds(List<String> ids) {
        Query query = new Query();
        SceneFiled.API_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class);
    }

    @Override
    public List<SceneCaseApiEntity> findSceneCaseApiBySceneCaseIdAndIsExecute(String sceneCaseId, Boolean isExecute) {
        Query query = new Query();
        SceneFiled.SCENE_CASE_ID.is(sceneCaseId).ifPresent(query::addCriteria);
        SceneFiled.API_IS_EXECUTE.is(isExecute).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class);
    }

    @Override
    public Boolean deleteSceneCaseApiConn(List<String> caseTemplateApiId) {
        for (String id : caseTemplateApiId) {
            Query query = new Query();
            CaseTemplateApiConn build = CaseTemplateApiConn.builder().caseTemplateApiId(id)
                .execute(Boolean.TRUE).build();
            query.addCriteria(Criteria.where(SceneFiled.CASE_TEMPLATE_API_CONN_LIST.getFiled()).is(build));
            Update update = new Update();
            update.pullAll(SceneFiled.CASE_TEMPLATE_API_CONN_LIST.getFiled(), Lists.newArrayList(build).toArray());

            mongoTemplate.updateMulti(query, update, SceneCaseApiEntity.class);
            CaseTemplateApiConn buildFalse = CaseTemplateApiConn.builder().caseTemplateApiId(id)
                .execute(Boolean.FALSE).build();
            update.pullAll(SceneFiled.CASE_TEMPLATE_API_CONN_LIST.getFiled(), Lists.newArrayList(buildFalse).toArray());
            mongoTemplate.updateMulti(query, update, SceneCaseApiEntity.class);
        }
        return Boolean.TRUE;
    }


}
