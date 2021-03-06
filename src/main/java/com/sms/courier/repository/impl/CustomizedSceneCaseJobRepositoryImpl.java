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

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.field.SceneCaseJobField;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
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
    public List<CountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime) {
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
        return mongoTemplate.aggregate(aggregation, SceneCaseJobEntity.class, CountStatisticsResponse.class)
            .getMappedResults();
    }

}
