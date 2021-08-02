package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiTestCaseJobField.API_TEST_CASE_ID;
import static com.sms.satp.common.field.ApiTestCaseJobField.ENGINE_ID;
import static com.sms.satp.common.field.ApiTestCaseJobField.JOB_API_ID;
import static com.sms.satp.common.field.ApiTestCaseJobField.JOB_STATUS;
import static com.sms.satp.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonField.CREATE_USER_ID;
import static com.sms.satp.common.field.CommonField.ID;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiTestCaseJobRepositoryImpl implements CustomizedApiTestCaseJobRepository {

    private final MongoTemplate mongoTemplate;
    private static final String CASE_REPORT = "apiTestCase.jobApiTestCase.caseReport";
    private static final String CREATE_USER_NAME = "createUserName";
    private static final String MESSAGE = "message";
    private static final List<JobStatus> JOB_STATUSES = List.of(JobStatus.SUCCESS, JobStatus.FAIL);

    public CustomizedApiTestCaseJobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ApiTestCaseJobEntity> page(ApiTestCaseJobPageRequest apiTestCaseJobPageRequest) {
        Document document = new Document();
        document.put(CASE_REPORT, true);
        document.put(ID.getName(), true);
        document.put(JOB_STATUS.getName(), true);
        document.put(CREATE_USER_NAME, true);
        document.put(CREATE_DATE_TIME.getName(), true);
        document.put(MESSAGE, true);
        BasicQuery query = new BasicQuery(new Document(), document);
        API_TEST_CASE_ID.is(apiTestCaseJobPageRequest.getApiTestCaseId()).ifPresent(query::addCriteria);
        CREATE_USER_ID.in(apiTestCaseJobPageRequest.getUserIds()).ifPresent(query::addCriteria);
        JOB_API_ID.is((apiTestCaseJobPageRequest.getApiId())).ifPresent(query::addCriteria);
        JOB_STATUS.in(JOB_STATUSES).ifPresent(query::addCriteria);
        long count = mongoTemplate.count(query, ApiTestCaseJobEntity.class);
        if (count <= 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        PageDtoConverter.frontMapping(apiTestCaseJobPageRequest);
        Pageable pageable = PageDtoConverter.createPageable(apiTestCaseJobPageRequest);
        List<ApiTestCaseJobEntity> apiTestCaseJobs = mongoTemplate.find(query.with(pageable),
            ApiTestCaseJobEntity.class);
        return new PageImpl<>(apiTestCaseJobs, pageable, count);
    }

    @Override
    public ApiTestCaseJobEntity findRecentlyCaseReportByCaseId(String apiTestCaseId) {
        ApiTestCaseJobPageRequest pageRequest = ApiTestCaseJobPageRequest.builder().pageSize(1)
            .apiTestCaseId(apiTestCaseId)
            .pageNumber(1).build();
        Page<ApiTestCaseJobEntity> page = this.page(pageRequest);
        return page.stream().findFirst().orElse(ApiTestCaseJobEntity.builder().build());
    }

    @Override
    public void updateJobById(String id, String engineId, JobStatus jobStatus) {
        Query query = Query.query(Criteria.where(ID.getName()).is(id));
        Update update = new Update();
        update.set(ENGINE_ID.getName(), engineId);
        update.set(JOB_STATUS.getName(), jobStatus);
        mongoTemplate.updateFirst(query, update, ApiTestCaseJobEntity.class);
    }
}
