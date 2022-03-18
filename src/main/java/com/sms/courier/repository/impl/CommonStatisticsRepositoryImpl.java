package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.CREATE_USER_ID;
import static com.sms.courier.common.field.CommonField.CREATE_USER_NAME;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.CommonField.USERNAME;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.LookupOperation.newLookup;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.repository.CommonStatisticsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CommonStatisticsRepositoryImpl implements CommonStatisticsRepository {

    private final MongoTemplate mongoTemplate;

    public CommonStatisticsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <T> List<CountStatisticsResponse> getGroupDayCount(List<String> projectIds,
        LocalDateTime dateTime, Class<T> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).in(projectIds)));
        setGroupDayQuery(aggregationOperations, dateTime);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, entityClass, CountStatisticsResponse.class).getMappedResults();
    }

    @Override
    public <T> List<CountStatisticsResponse> getGroupDayCount(LocalDateTime dateTime, Class<T> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        setGroupDayQuery(aggregationOperations, dateTime);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, entityClass, CountStatisticsResponse.class).getMappedResults();
    }

    @Override
    public <T> List<CaseCountUserStatisticsResponse> getGroupUserCount(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations
            .add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).in(projectIds)));
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.match(Criteria.where(CREATE_DATE_TIME.getName()).gt(dateTime)));
        aggregationOperations
            .add(newLookup().from(CollectionName.USER.getName()).localField(CREATE_USER_ID.getName())
                .foreignField(ID.getName()).as("user"));

        aggregationOperations.add(Aggregation.group("user." + USERNAME.getName()).count().as(Constants.COUNT));
        aggregationOperations
            .add(project().and(ID.getName()).as(USERNAME.getName()).and(Constants.COUNT).as(Constants.COUNT));
        aggregationOperations.add(Aggregation.sort(Direction.DESC, Constants.COUNT));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, entityClass, CaseCountUserStatisticsResponse.class)
            .getMappedResults();
    }

    @Override
    public <T> List<CaseCountUserStatisticsResponse> getGroupUserCountByJob(List<String> projectIds,
        LocalDateTime dateTime, Class<T> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations
            .add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).in(projectIds)));
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.match(Criteria.where(CREATE_DATE_TIME.getName()).gt(dateTime)));

        aggregationOperations.add(Aggregation.group(CREATE_USER_NAME.getName()).count().as(Constants.COUNT));
        aggregationOperations
            .add(project().and(ID.getName()).as(USERNAME.getName()).and(Constants.COUNT).as(Constants.COUNT));
        aggregationOperations.add(Aggregation.sort(Direction.DESC, Constants.COUNT));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, entityClass, CaseCountUserStatisticsResponse.class)
            .getMappedResults();
    }

    private void setGroupDayQuery(List<AggregationOperation> aggregationOperations, LocalDateTime dateTime) {
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.match(Criteria.where(CREATE_DATE_TIME.getName()).gt(dateTime)));
        aggregationOperations
            .add(project().and(CREATE_DATE_TIME.getName()).dateAsFormattedString(Constants.GROUP_DAY_FORMATTER)
                .as(Constants.DAY));
        aggregationOperations.add(Aggregation.group(Constants.DAY).count().as(Constants.COUNT));
        aggregationOperations
            .add(project().and(ID.getName()).as(Constants.DAY).and(Constants.COUNT).as(Constants.COUNT));
        aggregationOperations.add(Aggregation.sort(Direction.DESC, Constants.DAY));
    }

}
