package com.sms.courier.repository.impl;

import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.CommonField.WORKSPACE_ID;

import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import com.sms.courier.repository.CustomizedFunctionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedFunctionRepositoryImpl implements CustomizedFunctionRepository {

    private final MongoTemplate mongoTemplate;

    public CustomizedFunctionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<LoadFunctionResponse> loadFunction(String projectId, String workspaceId, Class<?> entityClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        PROJECT_ID.is(projectId).ifPresent(criteria -> aggregationOperations.add(Aggregation.match(criteria)));
        WORKSPACE_ID.is(workspaceId).ifPresent(criteria -> aggregationOperations.add(Aggregation.match(criteria)));
        REMOVE.is(false).ifPresent(criteria -> aggregationOperations.add(Aggregation.match(criteria)));
        ProjectionOperation projectionOperation = Aggregation.project("functionKey").and("functionParams.key")
            .as("functionParams");
        if (entityClass == ProjectFunctionEntity.class) {
            projectionOperation = projectionOperation.and("removed").as("global");
        }
        aggregationOperations.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        List<LoadFunctionResponse> results = mongoTemplate
            .aggregate(aggregation, entityClass, LoadFunctionResponse.class).getMappedResults();
        return Objects.requireNonNullElse(results, new ArrayList<>());
    }

}
