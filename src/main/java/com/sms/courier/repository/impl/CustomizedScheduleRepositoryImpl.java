package com.sms.courier.repository.impl;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.field.ScheduleField;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.repository.CustomizedScheduleRepository;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedScheduleRepositoryImpl implements CustomizedScheduleRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedScheduleRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Boolean removeCaseIds(List<String> caseIds) {
        Query query = new Query();
        ScheduleField.CASE_TYPE.is(CaseType.SCENE_CASE.getCode()).ifPresent(query::addCriteria);
        Update update = new Update();
        update.pullAll(ScheduleField.CASE_IDS.getName(), Lists.newArrayList(caseIds).toArray());
        mongoTemplate.updateMulti(query, update, ScheduleEntity.class);
        return Boolean.TRUE;
    }

}
