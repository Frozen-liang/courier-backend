package com.sms.satp.repository.impl;

import static com.sms.satp.common.constant.CommonFiled.ID;
import static com.sms.satp.common.constant.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.constant.CommonFiled.REMOVE;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.repository.CommonDeleteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDeleteRepositoryImpl implements CommonDeleteRepository {

    protected final MongoTemplate mongoTemplate;
    private static final int MODIFY_COUNT = 1;

    public CommonDeleteRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Boolean deleteById(String id, Class<?> entityClass) {
        Query query = new Query(Criteria.where(ID).is(id));
        Update update = Update.update(REMOVE, Boolean.TRUE);
        update.set(MODIFY_DATE_TIME, LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, entityClass);
        if (updateResult.getModifiedCount() == MODIFY_COUNT) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteByIds(List<String> ids, Class<?> entityClass) {
        Query query = new Query(Criteria.where(ID).in(ids));
        Update update = Update.update(REMOVE, Boolean.TRUE);
        update.set(MODIFY_DATE_TIME, LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entityClass);
        if (updateResult.getModifiedCount() > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
