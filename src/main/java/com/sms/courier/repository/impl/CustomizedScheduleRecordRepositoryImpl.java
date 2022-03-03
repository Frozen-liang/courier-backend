package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.ScheduleRecordField.EXECUTE_RECORD;
import static com.sms.courier.common.field.ScheduleRecordField.JOB_ID;
import static com.sms.courier.common.field.ScheduleRecordField.TEST_COMPLETION_TIME;

import com.sms.courier.common.field.CommonField;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.repository.CustomizedScheduleRecordRepository;
import java.time.LocalDateTime;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedScheduleRecordRepositoryImpl implements CustomizedScheduleRecordRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedScheduleRecordRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ScheduleRecordEntity findAndModifyExecuteRecord(String id, ScheduleSceneCaseJobEntity scheduleCaseJob) {
        Query query = new Query();
        Document document = new Document();
        document.put(JOB_ID.getName(), scheduleCaseJob.getId());
        query.addCriteria(Criteria.where(CommonField.ID.getName()).is(id));
        Update update = new Update();
        update.pull(EXECUTE_RECORD.getName(), document);
        update.set(TEST_COMPLETION_TIME.getName(), LocalDateTime.now());
        return mongoTemplate
            .findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ScheduleRecordEntity.class);
    }

}
