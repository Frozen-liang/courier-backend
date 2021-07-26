package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiField.GROUP_ID;
import static com.sms.satp.common.field.ApiField.TAG_ID;
import static com.sms.satp.common.field.CommonField.ID;

import com.sms.satp.common.field.CommonField;
import com.sms.satp.common.field.SceneField;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CustomizedSceneCaseRepositoryImpl implements CustomizedSceneCaseRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonRepository commonRepository;

    public CustomizedSceneCaseRepositoryImpl(MongoTemplate mongoTemplate,
        CommonRepository commonRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonRepository = commonRepository;
    }

    @Override
    public Page<SceneCaseResponse> search(SearchSceneCaseRequest searchSceneCaseRequest, ObjectId projectId) {
        PageDtoConverter.frontMapping(searchSceneCaseRequest);
        ArrayList<AggregationOperation> aggregationOperations = new ArrayList<>();
        Query query = new Query();

        LookupOperation apiGroupLookupOperation =
            LookupOperation.newLookup().from("SceneCaseGroup").localField(GROUP_ID.getName())
                .foreignField(ID.getName())
                .as("sceneCaseGroup");
        LookupOperation apiTagLookupOperation =
            LookupOperation.newLookup().from("ApiTag").localField(TAG_ID.getName())
                .foreignField(ID.getName())
                .as("apiTag");

        aggregationOperations.add(apiTagLookupOperation);
        aggregationOperations.add(apiGroupLookupOperation);

        buildCriteria(searchSceneCaseRequest, query, projectId, aggregationOperations);

        Sort sort = PageDtoConverter.createSort(searchSceneCaseRequest);
        aggregationOperations.add(Aggregation.sort(sort));

        int skipRecord = searchSceneCaseRequest.getPageNumber() * searchSceneCaseRequest.getPageSize();
        aggregationOperations.add(Aggregation.limit(searchSceneCaseRequest.getPageSize()));
        aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));

        ProjectionOperation projectionOperation = Aggregation.project(SceneCaseResponse.class);
        projectionOperation = projectionOperation.and("sceneCaseGroup.name").as("groupName");
        projectionOperation = projectionOperation.and("apiTag.tagName").as("tagName");
        aggregationOperations.add(projectionOperation);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        long count = mongoTemplate.count(query, SceneCaseEntity.class);
        if (count == 0L || skipRecord >= count) {
            return Page.empty();
        }
        List<SceneCaseResponse> records = mongoTemplate
            .aggregate(aggregation, SceneCaseEntity.class, SceneCaseResponse.class)
            .getMappedResults();
        return new PageImpl<SceneCaseResponse>(records,
            PageRequest.of(searchSceneCaseRequest.getPageNumber(), searchSceneCaseRequest.getPageSize(), sort), count);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonRepository.deleteByIds(ids, SceneCaseEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonRepository.recover(ids, SceneCaseEntity.class);
    }

    @Override
    public List<SceneCaseEntity> getIdsByGroupId(String id) {
        Query query = new Query();
        query.fields().include(ID.getName());
        CommonField.GROUP_ID.is(id).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, SceneCaseEntity.class);
    }

    private void buildCriteria(SearchSceneCaseRequest searchSceneCaseRequest, Query query,
        ObjectId projectId, ArrayList<AggregationOperation> aggregationOperations) {
        CommonField.PROJECT_ID.is(projectId).ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        CommonField.REMOVE.is(searchSceneCaseRequest.isRemoved())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.NAME.like(searchSceneCaseRequest.getName())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.GROUP_ID.is(searchSceneCaseRequest.getGroupId())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.TEST_STATUS.in(searchSceneCaseRequest.getTestStatus())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.TAG_ID.in(searchSceneCaseRequest.getTagId())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.PRIORITY.in(searchSceneCaseRequest.getPriority())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneField.CREATE_USER_NAME.in(searchSceneCaseRequest.getCreateUserName())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
    }

    private void addCriteria(Criteria criteria, Query query, List<AggregationOperation> aggregationOperations) {
        query.addCriteria(criteria);
        aggregationOperations.add(Aggregation.match(criteria));
    }

}
