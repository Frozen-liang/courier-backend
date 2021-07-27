package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiFiled.GROUP_ID;
import static com.sms.satp.common.field.ApiFiled.TAG_ID;
import static com.sms.satp.common.field.CommonFiled.ID;

import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
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
public class CustomizedCaseTemplateRepositoryImpl implements CustomizedCaseTemplateRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonDeleteRepository commonDeleteRepository;

    public CustomizedCaseTemplateRepositoryImpl(MongoTemplate mongoTemplate,
        CommonDeleteRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
    }

    @Override
    public Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId) {
        PageDtoConverter.frontMapping(searchDto);
        ArrayList<AggregationOperation> aggregationOperations = new ArrayList<>();
        Query query = new Query();

        LookupOperation apiTagLookupOperation =
            LookupOperation.newLookup().from("ApiTag").localField(TAG_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("apiTag");
        LookupOperation apiGroupLookupOperation =
            LookupOperation.newLookup().from("CaseTemplateGroup").localField(GROUP_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("caseTemplateGroup");

        aggregationOperations.add(apiTagLookupOperation);
        aggregationOperations.add(apiGroupLookupOperation);

        buildCriteria(searchDto, query, projectId, aggregationOperations);

        int skipRecord = searchDto.getPageNumber() * searchDto.getPageSize();
        aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
        aggregationOperations.add(Aggregation.limit(searchDto.getPageSize()));

        Sort sort = PageDtoConverter.createSort(searchDto);
        aggregationOperations.add(Aggregation.sort(sort));

        ProjectionOperation projectionOperation = Aggregation.project(CaseTemplateResponse.class);
        projectionOperation = projectionOperation.and("apiTag.tagName").as("tagName");
        projectionOperation = projectionOperation.and("caseTemplateGroup.name").as("groupName");
        aggregationOperations.add(projectionOperation);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        long count = mongoTemplate.count(query, CaseTemplateEntity.class);
        if (count == 0L || skipRecord >= count) {
            return Page.empty();
        }
        List<CaseTemplateResponse> records = mongoTemplate
            .aggregate(aggregation, CaseTemplateEntity.class, CaseTemplateResponse.class)
            .getMappedResults();
        return new PageImpl<CaseTemplateResponse>(records,
            PageRequest.of(searchDto.getPageNumber(), searchDto.getPageSize(), sort), count);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonDeleteRepository.deleteByIds(ids, CaseTemplateEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonDeleteRepository.recover(ids, CaseTemplateEntity.class);
    }

    @Override
    public List<CaseTemplateEntity> getCaseTemplateIdsByGroupIds(List<String> ids) {
        Query query = new Query();
        query.fields().include(ID.getFiled());
        CommonFiled.GROUP_ID.in(ids).ifPresent(query::addCriteria);
        return mongoTemplate.find(query, CaseTemplateEntity.class);
    }

    private void buildCriteria(CaseTemplateSearchRequest searchRequest, Query query,
        ObjectId projectId, ArrayList<AggregationOperation> aggregationOperations) {
        CommonFiled.PROJECT_ID.is(projectId).ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        CommonFiled.REMOVE.is(searchRequest.isRemoved())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneFiled.TEST_STATUS.in(searchRequest.getTestStatus())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneFiled.TAG_ID.in(searchRequest.getTagId())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneFiled.NAME.like(searchRequest.getName())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneFiled.GROUP_ID.is(searchRequest.getGroupId())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        SceneFiled.CREATE_USER_NAME.in(searchRequest.getCreateUserName())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
    }

    private void addCriteria(Criteria criteria, Query query, List<AggregationOperation> aggregationOperations) {
        query.addCriteria(criteria);
        aggregationOperations.add(Aggregation.match(criteria));
    }

}
