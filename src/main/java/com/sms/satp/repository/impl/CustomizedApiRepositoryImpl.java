package com.sms.satp.repository.impl;

import static com.sms.satp.common.enums.OperationModule.API_GROUP;
import static com.sms.satp.common.enums.OperationModule.API_TAG;
import static com.sms.satp.common.field.ApiFiled.API_NAME;
import static com.sms.satp.common.field.ApiFiled.API_PATH;
import static com.sms.satp.common.field.ApiFiled.API_PROTOCOL;
import static com.sms.satp.common.field.ApiFiled.API_STATUS;
import static com.sms.satp.common.field.ApiFiled.GROUP_ID;
import static com.sms.satp.common.field.ApiFiled.REQUEST_METHOD;
import static com.sms.satp.common.field.ApiFiled.TAG_ID;
import static com.sms.satp.common.field.CommonFiled.ID;
import static com.sms.satp.common.field.CommonFiled.MODIFY_DATE_TIME;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiRepositoryImpl implements CustomizedApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonDeleteRepository commonDeleteRepository;
    private final ApiGroupRepository apiGroupRepository;

    public CustomizedApiRepositoryImpl(MongoTemplate mongoTemplate, CommonDeleteRepository commonDeleteRepository,
        ApiGroupRepository apiGroupRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
        this.apiGroupRepository = apiGroupRepository;
    }


    @Override
    public Optional<ApiResponse> findById(String id) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        createLookUpOperation(aggregationOperations);
        ProjectionOperation projectionOperation = Aggregation.project(ApiResponse.class);
        projectionOperation = projectionOperation.and("apiTag.tagName").as("tagName");
        projectionOperation = projectionOperation.and("apiGroup.name").as("groupName");
        aggregationOperations.add(projectionOperation);
        aggregationOperations.add(Aggregation.match(Criteria.where(ID.getFiled()).is(id)));
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        List<ApiResponse> records = mongoTemplate.aggregate(aggregation, ApiEntity.class, ApiResponse.class)
            .getMappedResults();
        return records.size() > 0 ? Optional.of(records.get(0)) : Optional.empty();
    }

    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        PageDtoConverter.frontMapping(apiPageRequest);
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        Query query = new Query();

        createLookUpOperation(aggregationOperations);

        buildCriteria(apiPageRequest, query, aggregationOperations);

        Sort sort = PageDtoConverter.createSort(apiPageRequest);
        aggregationOperations.add(Aggregation.sort(sort));

        int skipRecord = apiPageRequest.getPageNumber() * apiPageRequest.getPageSize();
        aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
        aggregationOperations.add(Aggregation.limit(apiPageRequest.getPageSize()));

        ProjectionOperation projectionOperation = Aggregation.project(ApiResponse.class);
        projectionOperation = projectionOperation.and("apiTag.tagName").as("tagName");
        projectionOperation = projectionOperation.and("apiGroup.name").as("groupName");
        aggregationOperations.add(projectionOperation);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        long count = mongoTemplate.count(query, ApiEntity.class);
        if (count == 0L || skipRecord >= count) {
            return Page.empty();
        }
        List<ApiResponse> records = mongoTemplate.aggregate(aggregation, ApiEntity.class, ApiResponse.class)
            .getMappedResults();
        return new PageImpl<ApiResponse>(records,
            PageRequest.of(apiPageRequest.getPageNumber(), apiPageRequest.getPageSize(), sort), count);
    }

    @Override
    public Boolean deleteById(String id) {
        return commonDeleteRepository.deleteById(id, ApiEntity.class);
    }

    @Override
    public Boolean deleteByIds(List<String> ids) {
        return commonDeleteRepository.deleteByIds(ids, ApiEntity.class);
    }

    @Override
    public Boolean recover(List<String> ids) {
        return commonDeleteRepository.recover(ids, ApiEntity.class);
    }

    @Override
    public void deleteByGroupIds(List<String> groupIds) {
        Query query = new Query(Criteria.where(GROUP_ID.getFiled()).in(groupIds));
        Update update = Update.update(REMOVE.getFiled(), Boolean.TRUE);
        update.set(MODIFY_DATE_TIME.getFiled(), LocalDateTime.now());
        mongoTemplate.updateMulti(query, update, ApiEntity.class);
    }

    private void createLookUpOperation(List<AggregationOperation> aggregationOperations) {
        LookupOperation apiTagLookupOperation =
            LookupOperation.newLookup().from(API_TAG.getCollectionName()).localField(TAG_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("apiTag");
        LookupOperation apiGroupLookupOperation =
            LookupOperation.newLookup().from(API_GROUP.getCollectionName()).localField(GROUP_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("apiGroup");
        aggregationOperations.add(apiTagLookupOperation);
        aggregationOperations.add(apiGroupLookupOperation);
    }

    private void buildCriteria(ApiPageRequest apiPageRequest, Query query,
        List<AggregationOperation> aggregationOperations) {
        REMOVE.is(apiPageRequest.isRemoved())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        PROJECT_ID.is(apiPageRequest.getProjectId())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        API_NAME.like(apiPageRequest.getApiName())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        API_PATH.like(apiPageRequest.getApiPath())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        API_PROTOCOL.in(apiPageRequest.getApiProtocol())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        API_STATUS.in(apiPageRequest.getApiStatus())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        ObjectId groupId = apiPageRequest.getGroupId();
        if (Objects.nonNull(groupId)) {
            apiGroupRepository.findById(groupId.toString()).ifPresent((apiGroupEntity -> {
                List<ObjectId> ids = apiGroupRepository.findAllByPathContains(apiGroupEntity.getRealGroupId())
                    .map((entity) -> new ObjectId(entity.getId())).collect(
                        Collectors.toList());
                GROUP_ID.in(ids)
                    .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
            }));
        }
        REQUEST_METHOD.in(apiPageRequest.getRequestMethod())
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
        TAG_ID.in(apiPageRequest.getTagId()).ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations));
    }

    private void addCriteria(Criteria criteria, Query query, List<AggregationOperation> aggregationOperations) {
        query.addCriteria(criteria);
        aggregationOperations.add(Aggregation.match(criteria));
    }

}
