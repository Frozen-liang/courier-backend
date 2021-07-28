package com.sms.satp.repository.impl;

import static com.sms.satp.common.enums.OperationModule.USER;
import static com.sms.satp.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonField.CREATE_USER_ID;
import static com.sms.satp.common.field.CommonField.ID;
import static com.sms.satp.common.field.CommonField.MODIFY_DATE_TIME;
import static com.sms.satp.common.field.CommonField.REMOVE;
import static com.sms.satp.common.field.UserField.NICKNAME;
import static com.sms.satp.common.field.UserField.USERNAME;

import com.mongodb.client.result.UpdateResult;
import com.sms.satp.common.field.Field;
import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.mongo.LookupField;
import com.sms.satp.entity.mongo.LookupVo;
import com.sms.satp.entity.mongo.QueryVo;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class CommonRepositoryImpl implements CommonRepository {

    protected final MongoTemplate mongoTemplate;
    private static final int MODIFY_COUNT = 1;

    public CommonRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Boolean deleteById(String id, Class<?> entityClass) {
        Query query = new Query(Criteria.where(ID.getName()).is(id));
        Update update = Update.update(REMOVE.getName(), Boolean.TRUE);
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, entityClass);
        return updateResult.getModifiedCount() == MODIFY_COUNT;
    }

    @Override
    public Boolean deleteByIds(List<String> ids, Class<?> entityClass) {
        Query query = new Query(Criteria.where(ID.getName()).in(ids));
        Update update = Update.update(REMOVE.getName(), Boolean.TRUE);
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entityClass);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public Boolean removeTags(@NonNull Field filed, @NonNull List<String> tagIds, @NonNull Class<?> entityClass) {
        Query query = new Query();
        filed.in(tagIds).ifPresent(query::addCriteria);
        Update update = new Update();
        update.pullAll(filed.getName(), tagIds.toArray());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entityClass);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public Boolean recover(List<String> ids, Class<?> entityClass) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        Query query = new Query(Criteria.where(ID.getName()).in(ids));
        Update update = Update.update(REMOVE.getName(), Boolean.FALSE);
        update.set(MODIFY_DATE_TIME.getName(), LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entityClass);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public <T> Optional<T> findById(String id, String collectionName, LookupVo lookupVo, Class<T> responseClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(Criteria.where(ID.getName()).is(id)));
        ProjectionOperation projectionOperation = getProjectionOperation(responseClass);
        addLookupOperation(List.of(lookupVo), responseClass, aggregationOperations);
        aggregationOperations.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        T result = mongoTemplate.aggregate(aggregation, collectionName, responseClass).getUniqueMappedResult();
        return Optional.ofNullable(result);
    }


    @Override
    public <T> Optional<T> findById(String id, String collectionName, List<LookupVo> lookupVos,
        Class<T> responseClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        aggregationOperations.add(Aggregation.match(Criteria.where(ID.getName()).is(id)));
        ProjectionOperation projectionOperation = addLookupOperation(lookupVos, responseClass, aggregationOperations);
        aggregationOperations.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        T result = mongoTemplate.aggregate(aggregation, collectionName, responseClass).getUniqueMappedResult();
        return Optional.ofNullable(result);
    }

    @Override
    public <T> List<T> listLookupUser(String collectionName, List<Optional<Criteria>> criteriaList,
        Class<T> responseClass) {
        List<LookupField> lookupFields = List.of(
            LookupField.builder().field(USERNAME).alias("createUsername").build(),
            LookupField.builder().field(NICKNAME).alias("createNickname").build()
        );
        LookupVo lookupVo = LookupVo.builder()
            .from(USER)
            .localField(CREATE_USER_ID)
            .foreignField(ID).as("user")
            .queryFields(lookupFields).build();
        return this.list(collectionName, lookupVo, criteriaList, responseClass);
    }

    public <T> List<T> list(QueryVo query, Class<T> responseClass) {
        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        query.getCriteriaList().forEach((criteriaOptional) -> criteriaOptional
            .ifPresent(criteria -> aggregationOperations.add(Aggregation.match(criteria))));
        ProjectionOperation projectionOperation = addLookupOperation(query.getLookupVo(), responseClass,
            aggregationOperations);
        aggregationOperations.add(Aggregation.sort(Direction.DESC, CREATE_DATE_TIME.getName()));
        aggregationOperations.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);
        return mongoTemplate.aggregate(aggregation, query.getCollectionName(), responseClass).getMappedResults();
    }

    @Override
    public <T> List<T> list(String collectionName, LookupVo lookupVo, List<Optional<Criteria>> criteriaList,
        Class<T> responseClass) {
        QueryVo queryVo =
            QueryVo.builder().collectionName(collectionName).lookupVo(List.of(lookupVo)).criteriaList(criteriaList)
                .build();
        return this.list(queryVo, responseClass);
    }

    @Override
    public <T> Page<T> page(QueryVo queryVo, PageDto pageRequest, Class<T> responseClass) {
        PageDtoConverter.frontMapping(pageRequest);

        List<AggregationOperation> aggregationOperations = new ArrayList<>();
        Sort sort = PageDtoConverter.createSort(pageRequest);

        final ProjectionOperation projectionOperation = addLookupOperation(queryVo.getLookupVo(), responseClass,
            aggregationOperations);

        Query query = new Query();
        queryVo.getCriteriaList().forEach((criteriaOptional) -> criteriaOptional
            .ifPresent(criteria -> addCriteria(criteria, query, aggregationOperations)));

        aggregationOperations.add(Aggregation.sort(sort));
        int skipRecord = pageRequest.getPageNumber() * pageRequest.getPageSize();
        aggregationOperations.add(Aggregation.skip(Long.valueOf(skipRecord)));
        aggregationOperations.add(Aggregation.limit(pageRequest.getPageSize()));
        aggregationOperations.add(projectionOperation);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperations);

        long count = mongoTemplate.count(query, queryVo.getCollectionName());
        if (count == 0L || skipRecord >= count) {
            return Page.empty();
        }

        List<T> records = mongoTemplate.aggregate(aggregation, queryVo.getCollectionName(), responseClass)
            .getMappedResults();

        return new PageImpl<T>(records,
            PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort), count);
    }

    private <T> ProjectionOperation getProjectionOperation(Class<T> responseClass) {
        return Aggregation.project(responseClass);
    }

    private <T> ProjectionOperation addLookupOperation(List<LookupVo> lookupVos, Class<T> responseClass,
        List<AggregationOperation> aggregationOperations) {
        ProjectionOperation projectionOperation = getProjectionOperation(responseClass);
        for (LookupVo lookupVo : lookupVos) {
            LookupOperation lookupOperation =
                LookupOperation.newLookup().from(lookupVo.getFrom().getCollectionName())
                    .localField(lookupVo.getLocalField().getName())
                    .foreignField(lookupVo.getForeignField().getName())
                    .as(lookupVo.getAs());
            aggregationOperations.add(lookupOperation);
            for (LookupField queryField : lookupVo.getQueryFields()) {
                projectionOperation = projectionOperation
                    .and(lookupVo.getAs() + "." + queryField.getField().getName())
                    .as(StringUtils.defaultString(queryField.getAlias(), queryField.getField().getName()));
            }
        }
        return projectionOperation;
    }

    private void addCriteria(Criteria criteria, Query query, List<AggregationOperation> aggregationOperations) {
        query.addCriteria(criteria);
        aggregationOperations.add(Aggregation.match(criteria));
    }

}
