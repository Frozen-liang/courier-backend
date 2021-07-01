package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiTestCaseJobFiled.API_TEST_CASE_ID;
import static com.sms.satp.common.field.CommonFiled.API_ID;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.CREATE_USER_ID;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.repository.CustomizedApiTestCaseJobRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.util.Collections;
import java.util.List;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiTestCaseJobRepositoryImpl implements CustomizedApiTestCaseJobRepository {

    private final MongoTemplate mongoTemplate;
    private static final String CASE_REPORT = "apiTestCase.jobApiTestCase.caseReport";
    private static final String JOB_STATUS = "jobStatus";
    private static final String CREATE_USER_NAME = "createUserName";
    private static final String MESSAGE = "message";

    public CustomizedApiTestCaseJobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ApiTestCaseJob> page(ApiTestCaseJobPageRequest apiTestCaseJobPageRequest) {
        Document document = new Document();
        document.put(CASE_REPORT, true);
        document.put(JOB_STATUS, true);
        document.put(CREATE_USER_NAME, true);
        document.put(CREATE_DATE_TIME.getFiled(), true);
        document.put(MESSAGE, true);
        BasicQuery query = new BasicQuery(new Document(), document);
        API_TEST_CASE_ID.is(apiTestCaseJobPageRequest.getApiTestCaseId()).ifPresent(query::addCriteria);
        CREATE_USER_ID.in(apiTestCaseJobPageRequest.getUserIds()).ifPresent(query::addCriteria);
        API_ID.is((apiTestCaseJobPageRequest.getApiId())).ifPresent(query::addCriteria);
        long count = mongoTemplate.count(query, ApiTestCaseJob.class);
        if (count <= 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        PageDtoConverter.frontMapping(apiTestCaseJobPageRequest);
        Pageable pageable = PageDtoConverter.createPageable(apiTestCaseJobPageRequest);
        List<ApiTestCaseJob> apiTestCaseJobs = mongoTemplate.find(query.with(pageable),
            ApiTestCaseJob.class);
        return new PageImpl<>(apiTestCaseJobs, pageable, count);
    }
}
