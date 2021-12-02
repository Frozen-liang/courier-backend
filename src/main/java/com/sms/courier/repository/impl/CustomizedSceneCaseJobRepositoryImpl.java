package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.SceneCaseJobField.DELAY_TIME_TOTAL_TIME_COST;
import static com.sms.courier.common.field.SceneCaseJobField.ENVIRONMENT;
import static com.sms.courier.common.field.SceneCaseJobField.INFO_LIST;
import static com.sms.courier.common.field.SceneCaseJobField.PARAMS_TOTAL_TIME_COST;
import static com.sms.courier.common.field.SceneCaseJobField.TOTAL_TIME_COST;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.google.common.collect.Lists;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.field.SceneCaseJobField;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseJobRepositoryImpl implements CustomizedSceneCaseJobRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedSceneCaseJobRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SceneCaseJobEntity> page(SceneCaseJobRequest sceneCaseJobRequest) {
        PageDtoConverter.frontMapping(sceneCaseJobRequest);
        BasicQuery query = buildQueryFiled();
        CommonField.CREATE_USER_ID.in(sceneCaseJobRequest.getUserIds()).ifPresent(query::addCriteria);
        SceneField.SCENE_CASE_ID.is(sceneCaseJobRequest.getSceneCaseId()).ifPresent(query::addCriteria);
        SceneField.CASE_TEMPLATE_ID.is(sceneCaseJobRequest.getCaseTemplateId()).ifPresent(query::addCriteria);
        SceneField.JOB_ENV_ID.in(sceneCaseJobRequest.getEnvId()).ifPresent(query::addCriteria);
        CommonField.JOB_STATUS.in(Lists.newArrayList(JobStatus.SUCCESS.getCode(), JobStatus.FAIL.getCode()))
            .ifPresent(query::addCriteria);

        long total = mongoTemplate.count(query, SceneCaseJobEntity.class);
        Sort sort = Sort.by(Direction.fromString(sceneCaseJobRequest.getOrder()), sceneCaseJobRequest.getSort());
        Pageable pageable = PageRequest
            .of(sceneCaseJobRequest.getPageNumber(), sceneCaseJobRequest.getPageSize(), sort);
        List<SceneCaseJobEntity> sceneCaseJobList = mongoTemplate.find(query.with(pageable), SceneCaseJobEntity.class);
        return new PageImpl<>(sceneCaseJobList, pageable, total);
    }

    private BasicQuery buildQueryFiled() {
        Document document = new Document();
        document.put(SceneCaseJobField.API_TEST_CASE.getName(), true);
        document.put(SceneCaseJobField.JOB_STATUS.getName(), true);
        document.put(SceneCaseJobField.CREATE_USER_NAME.getName(), true);
        document.put(CREATE_DATE_TIME.getName(), true);
        document.put(SceneCaseJobField.MESSAGE.getName(), true);
        document.put(TOTAL_TIME_COST.getName(), true);
        document.put(PARAMS_TOTAL_TIME_COST.getName(), true);
        document.put(DELAY_TIME_TOTAL_TIME_COST.getName(), true);
        document.put(INFO_LIST.getName(), true);
        document.put(ENVIRONMENT.getName(), true);
        return new BasicQuery(new Document(), document);
    }

    @Override
    public List<CaseCountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(projectIds)) {
            aggregationOperations.add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).in(projectIds)));
        }
        aggregationOperations.add(Aggregation.match(Criteria.where(CREATE_DATE_TIME.getName()).gt(dateTime)));
        aggregationOperations
            .add(project().and(CREATE_DATE_TIME.getName()).dateAsFormattedString(Constants.GROUP_DAY_FORMATTER)
                .as(Constants.DAY));
        aggregationOperations.add(Aggregation.group(Constants.DAY).count().as(Constants.COUNT));
        aggregationOperations
            .add(project().and(ID.getName()).as(Constants.DAY).and(Constants.COUNT).as(Constants.COUNT));
        aggregationOperations.add(Aggregation.sort(Direction.DESC, Constants.DAY));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, SceneCaseJobEntity.class, CaseCountStatisticsResponse.class)
            .getMappedResults();
    }

}
