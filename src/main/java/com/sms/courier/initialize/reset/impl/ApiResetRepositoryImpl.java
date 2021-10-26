package com.sms.courier.initialize.reset.impl;

import static com.sms.courier.common.field.ApiField.OTHER_PROJECT_SCENE_CASE_COUNT;
import static com.sms.courier.common.field.ApiField.SCENE_CASE_COUNT;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;

import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.field.Field;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.initialize.ApiCaseCount;
import com.sms.courier.initialize.reset.ApiResetRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class ApiResetRepositoryImpl implements ApiResetRepository {

    private final MongoTemplate mongoTemplate;

    public ApiResetRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ApiCaseCount> findProjectIdAndGroupByApiId(String projectId, boolean removed) {
        List<AggregationOperation> aggregationOperations = Lists.newArrayList();
        aggregationOperations.add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).is(new ObjectId(projectId))));
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(removed)));
        aggregationOperations.add(Aggregation.group(CommonField.API_ID_GROUP_SEARCH.getName()).count().as("count"));
        aggregationOperations.add(Aggregation.project("count").and("apiId").previousOperation());
        Aggregation aggregation1 = Aggregation.newAggregation(aggregationOperations);
        AggregationResults<ApiCaseCount> results = mongoTemplate.aggregate(aggregation1, ApiTestCaseEntity.class,
            ApiCaseCount.class);
        return results.getMappedResults();
    }


    @Override
    public long updateCountFieldByIds(List<ApiCaseCount> caseCountList, Field filedName, boolean isAdd) {
        long count = 0;
        for (ApiCaseCount apiCaseCount : caseCountList) {
            if (StringUtils.isNotBlank(apiCaseCount.getApiId())) {
                Query query = new Query();
                query.addCriteria(Criteria.where(ID.getName()).is(new ObjectId(apiCaseCount.getApiId())));
                Update update = new Update();
                if (isAdd) {
                    update.inc(filedName.getName(), apiCaseCount.getCount());
                } else {
                    update.set(filedName.getName(), apiCaseCount.getCount());
                }
                UpdateResult result = mongoTemplate.updateMulti(query, update, ApiEntity.class);
                count += result.getMatchedCount();
            }
        }
        return count;
    }

    @Override
    public long resetSceneCaseCount(List<String> projectIds) {
        Query query = new Query();
        PROJECT_ID.in(projectIds).ifPresent(query::addCriteria);
        Update update = new Update();
        update.set(SCENE_CASE_COUNT.getName(), 0);
        update.set(OTHER_PROJECT_SCENE_CASE_COUNT.getName(), 0);
        UpdateResult result = mongoTemplate.updateMulti(query, update, ApiEntity.class);
        return result.getMatchedCount();
    }

    @Override
    public List<ApiCaseCount> findApiCountByProjectIdAndGroupByApiId(String projectId, boolean removed,
        boolean isNowObject) {
        List<AggregationOperation> aggregationOperations = Lists.newArrayList();
        aggregationOperations.add(Aggregation.match(Criteria.where(PROJECT_ID.getName()).is(new ObjectId(projectId))));
        if (isNowObject) {
            aggregationOperations.add(Aggregation
                .match(Criteria.where(SceneField.API_TEST_CASE_PROJECT_ID.getName()).is(new ObjectId(projectId))));
        } else {
            aggregationOperations.add(Aggregation
                .match(Criteria.where(SceneField.API_TEST_CASE_PROJECT_ID.getName()).ne(new ObjectId(projectId))));
        }
        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(removed)));
        aggregationOperations.add(Aggregation.match(Criteria.where(SceneField.API_TYPE.getName()).is(ApiType.API)));
        aggregationOperations
            .add(Aggregation.match(Criteria.where(SceneField.CASE_TEMPLATE_ID.getName()).exists(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.group(SceneField.API_ID_GROUP_SEARCH.getName()).count().as("count"));
        aggregationOperations.add(Aggregation.project("count").and("apiId").previousOperation());
        Aggregation aggregation1 = Aggregation.newAggregation(aggregationOperations);
        AggregationResults<ApiCaseCount> results = mongoTemplate.aggregate(aggregation1, SceneCaseApiEntity.class,
            ApiCaseCount.class);
        return results.getMappedResults();
    }


    @Override
    public List<String> findCaseTemplateIdByProjectIdAndRemoved(String projectId, boolean removed) {
        Document document = new Document();
        document.put(SceneField.CASE_TEMPLATE_ID.getName(), true);
        BasicQuery query = new BasicQuery(new Document(), document);
        PROJECT_ID.is(projectId).ifPresent(query::addCriteria);
        REMOVE.is(removed).ifPresent(query::addCriteria);
        SceneField.CASE_TEMPLATE_ID.exists(Boolean.TRUE).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseApiEntity.class).stream().map(SceneCaseApiEntity::getCaseTemplateId)
            .collect(
                Collectors.toList());
    }

    @Override
    public List<ApiCaseCount> findApiCountByProjectIdAndCaseTemplateIdAndGroupByApiId(String caseTemplateId,
        String projectId, boolean isNowProject) {
        List<AggregationOperation> aggregationOperations = Lists.newArrayList();
        if (isNowProject) {
            aggregationOperations.add(Aggregation
                .match(Criteria.where(SceneField.API_TEST_CASE_PROJECT_ID.getName()).is(new ObjectId(projectId))));
        } else {
            aggregationOperations.add(Aggregation
                .match(Criteria.where(SceneField.API_TEST_CASE_PROJECT_ID.getName()).ne(new ObjectId(projectId))));
        }

        aggregationOperations.add(Aggregation.match(Criteria.where(REMOVE.getName()).is(Boolean.FALSE)));
        aggregationOperations.add(Aggregation.match(Criteria.where(SceneField.API_TYPE.getName()).is(ApiType.API)));
        ObjectId caseTemplateObjectIds = new ObjectId(caseTemplateId);
        aggregationOperations
            .add(Aggregation.match(Criteria.where(SceneField.CASE_TEMPLATE_ID.getName()).is(caseTemplateObjectIds)));
        aggregationOperations.add(Aggregation.group(SceneField.API_ID_GROUP_SEARCH.getName()).count().as("count"));
        aggregationOperations.add(Aggregation.project("count").and("apiId").previousOperation());
        Aggregation aggregation1 = Aggregation.newAggregation(aggregationOperations);
        AggregationResults<ApiCaseCount> results = mongoTemplate.aggregate(aggregation1, CaseTemplateApiEntity.class,
            ApiCaseCount.class);
        return results.getMappedResults();
    }

}
