package com.sms.satp.repository.impl;

import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
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
        query.with(Sort.by(Direction.DESC, SceneFiled.ORDER.getFiled()));
        query.limit(1);
        return mongoTemplate.findOne(query, SceneCaseApi.class);
    }

}
