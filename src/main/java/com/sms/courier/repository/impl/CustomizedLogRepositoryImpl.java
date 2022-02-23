package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.REF_ID;
import static com.sms.courier.common.field.CommonField.SOURCE_ID;
import static com.sms.courier.common.field.LogField.OPERATION_DESC;
import static com.sms.courier.common.field.LogField.OPERATION_MODULE;
import static com.sms.courier.common.field.LogField.OPERATION_TYPE;
import static com.sms.courier.common.field.LogField.OPERATOR_ID;

import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.entity.log.LogEntity;
import com.sms.courier.repository.CustomizedLogRepository;
import com.sms.courier.utils.PageDtoConverter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedLogRepositoryImpl implements CustomizedLogRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedLogRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<LogEntity> page(LogPageRequest logPageRequest) {
        PageDtoConverter.frontMapping(logPageRequest);
        Query query = new Query();
        CREATE_DATE_TIME.lteAndGte(logPageRequest.getQueryBeginTime(), logPageRequest.getQueryEndTime())
            .ifPresent(query::addCriteria);
        REF_ID.isOrNotExist(logPageRequest.getRefId()).ifPresent(query::addCriteria);
        OPERATION_DESC.like(logPageRequest.getOperationDesc()).ifPresent(query::addCriteria);
        OPERATION_TYPE.in(logPageRequest.getOperationType()).ifPresent(query::addCriteria);
        OPERATION_MODULE.in(logPageRequest.getOperationModule()).ifPresent(query::addCriteria);
        OPERATOR_ID.in(logPageRequest.getOperatorId()).ifPresent(query::addCriteria);
        SOURCE_ID.in(logPageRequest.getSourceId()).ifPresent(query::addCriteria);
        long count = mongoTemplate.count(query, LogEntity.class);
        Pageable pageable = PageDtoConverter.createPageable(logPageRequest);
        List<LogEntity> logList = mongoTemplate.find(query.with(pageable), LogEntity.class);
        return new PageImpl<>(logList, pageable, count);
    }
}
