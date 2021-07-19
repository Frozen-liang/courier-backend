package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.CommonFiled.API_ID;
import static com.sms.satp.common.field.CommonFiled.MODIFY_DATE_TIME;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.CustomizedApiTestCaseRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiTestCaseRepositoryImpl implements CustomizedApiTestCaseRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonDeleteRepository commonDeleteRepository;
    private static final String STATUS = "status";

    public CustomizedApiTestCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonDeleteRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
    }


    @Override
    public void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status) {
        Query query = new Query();
        API_ID.in(apiIds).ifPresent(query::addCriteria);
        Update update = new Update();
        update.set(STATUS, status.getCode());
        update.set(MODIFY_DATE_TIME.getFiled(), LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteById(String id) {
        return commonDeleteRepository.deleteById(id, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonDeleteRepository.deleteByIds(ids, ApiTestCaseEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonDeleteRepository.recover(ids, ApiTestCaseEntity.class);
    }

}
