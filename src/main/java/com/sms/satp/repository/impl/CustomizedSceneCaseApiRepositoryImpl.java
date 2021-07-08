package com.sms.satp.repository.impl;

import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseApiRepositoryImpl implements CustomizedSceneCaseApiRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseApiRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SceneCaseApi findMaxOrderBySceneCaseId(String sceneCaseId) {
        Query query = new Query();
        SceneFiled.SCENE_CASE_ID.is(sceneCaseId).ifPresent(query::addCriteria);
        query.with(Sort.by(Direction.DESC, SceneFiled.ORDER.getFiled()));
        query.limit(1);
        return mongoTemplate.findOne(query, SceneCaseApi.class);
    }

    @Override
    public List<SceneCaseApi> findSceneCaseApiByApiIds(List<String> ids) {
        Query query = new Query();
        SceneFiled.API_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApi.class);
    }

}
