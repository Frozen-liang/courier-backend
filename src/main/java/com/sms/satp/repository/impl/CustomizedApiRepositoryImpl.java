package com.sms.satp.repository.impl;

import static com.sms.satp.common.field.ApiFiled.API_PROTOCOL;
import static com.sms.satp.common.field.ApiFiled.API_REQUEST_PARAM_TYPE;
import static com.sms.satp.common.field.ApiFiled.API_STATUS;
import static com.sms.satp.common.field.ApiFiled.GROUP_ID;
import static com.sms.satp.common.field.ApiFiled.REQUEST_METHOD;
import static com.sms.satp.common.field.ApiFiled.TAG_ID;
import static com.sms.satp.common.field.CommonFiled.ID;
import static com.sms.satp.common.field.CommonFiled.PROJECT_ID;
import static com.sms.satp.common.field.CommonFiled.REMOVE;

import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomizedApiRepositoryImpl implements CustomizedApiRepository {

    private final MongoTemplate mongoTemplate;
    private final CommonDeleteRepository commonDeleteRepository;

    public CustomizedApiRepositoryImpl(MongoTemplate mongoTemplate, CommonDeleteRepository commonDeleteRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commonDeleteRepository = commonDeleteRepository;
    }


    @Override
    public Page<ApiResponse> page(ApiPageRequest apiPageRequest) {
        ArrayList<AggregationOperation> aggregationOperations = new ArrayList<>();
        Query query = new Query();

        LookupOperation apiTagLookupOperation =
            LookupOperation.newLookup().from("ApiTag").localField(TAG_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("apiTag");
        LookupOperation apiGroupLookupOperation =
            LookupOperation.newLookup().from("ApiGroup").localField(GROUP_ID.getFiled())
                .foreignField(ID.getFiled())
                .as("ApiGroup");
        aggregationOperations.add(apiTagLookupOperation);
        aggregationOperations.add(apiGroupLookupOperation);

        buildCriteria(apiPageRequest, query, aggregationOperations);

        Sort sort = Sort.by(Direction.fromString(apiPageRequest.getOrder()), apiPageRequest.getSort());
        aggregationOperations.add(Aggregation.sort(sort));

        int skipRecord = apiPageRequest.getPageNumber() * apiPageRequest.getPageSize();
        aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
        aggregationOperations.add(Aggregation.limit(apiPageRequest.getPageSize()));

        ProjectionOperation project = Aggregation.project(ApiResponse.class);
        ProjectionOperation projectionOperation = project.andInclude("apiTag.tagName", "ApiGroup.groupName");
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

    private void buildCriteria(ApiPageRequest apiPageRequest, Query query,
        List<AggregationOperation> aggregationOperations) {
        Criteria projectIdCriteria = Criteria.where(PROJECT_ID.getFiled()).is(apiPageRequest.getProjectId());
        Criteria removedCriteria = Criteria.where(REMOVE.getFiled()).is(Boolean.FALSE);
        query.addCriteria(projectIdCriteria);
        query.addCriteria(removedCriteria);
        aggregationOperations.add(Aggregation.match(projectIdCriteria));
        aggregationOperations.add(Aggregation.match(removedCriteria));

        if (Objects.nonNull(apiPageRequest.getApiProtocol())) {
            Criteria criteria = Criteria.where(API_PROTOCOL.getFiled()).in(apiPageRequest.getApiProtocol());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getApiRequestParamType())) {
            Criteria criteria = Criteria.where(API_REQUEST_PARAM_TYPE.getFiled())
                .in(apiPageRequest.getApiRequestParamType());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getApiStatus())) {
            Criteria criteria = Criteria.where(API_STATUS.getFiled()).in(apiPageRequest.getApiStatus());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getGroupId())) {
            Criteria criteria = Criteria.where(GROUP_ID.getFiled()).in(apiPageRequest.getGroupId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getRequestMethod())) {
            Criteria criteria = Criteria.where(REQUEST_METHOD.getFiled())
                .in(apiPageRequest.getRequestMethod());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }
        if (Objects.nonNull(apiPageRequest.getTagId())) {
            Criteria criteria = Criteria.where(TAG_ID.getFiled()).in(apiPageRequest.getTagId());
            aggregationOperations.add(Aggregation.match(criteria));
            query.addCriteria(criteria);
        }

    }

}
