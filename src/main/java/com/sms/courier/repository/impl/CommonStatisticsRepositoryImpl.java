package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.repository.CommonStatisticsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
    public <T> List<CaseCountStatisticsResponse> getGroupDayCount(List<String> projectIds,
        LocalDateTime dateTime, Class<T> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(projectIds)) {
            aggregationOperations.add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).in(projectIds)));
        }
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.match(Criteria.where(CREATE_DATE_TIME.getName()).gt(dateTime)));
        aggregationOperations
            .add(project().and(CREATE_DATE_TIME.getName()).dateAsFormattedString(Constants.GROUP_DAY_FORMATTER)
                .as(Constants.DAY));
        aggregationOperations.add(Aggregation.group(Constants.DAY).count().as(Constants.COUNT));
        aggregationOperations
            .add(project().and(ID.getName()).as(Constants.DAY).and(Constants.COUNT).as(Constants.COUNT));
        aggregationOperations.add(Aggregation.sort(Direction.DESC, Constants.DAY));

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, entityClass, CaseCountStatisticsResponse.class).getMappedResults();
    }

}
