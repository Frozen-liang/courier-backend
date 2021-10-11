package com.sms.courier.repository;

import com.sms.courier.common.field.Field;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.entity.mongo.CustomQuery;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

public interface CommonRepository {

    Boolean deleteById(String id, Class<?> entityClass);

    Boolean deleteByIds(List<String> ids, Class<?> entityClass);

    Boolean removeTags(Field filed, List<String> tagIds, Class<?> entityClass);

    Boolean recover(List<String> ids, Class<?> entityClass);

    <T> Optional<T> findById(String id, String collectionName, LookupVo lookupVo, Class<T> responseClass);

    <T> Optional<T> findById(String id, String collectionName, List<LookupVo> lookupVo, Class<T> responseClass);

    <T> Optional<T> findById(String id, Class<T> entityClass);

    <T> List<T> listLookupUser(String collectionName, List<Optional<Criteria>> criteriaList, Class<T> responseClass);

    <T> List<T> list(QueryVo queryVo, Class<T> responseClass);

    <T> List<T> list(String collectionName, LookupVo lookupVo, List<Optional<Criteria>> criteriaList,
        Class<T> responseClass);

    <T> List<T> list(Query query, Class<T> entityClass);

    // 联表分页查询
    <T> Page<T> page(QueryVo queryVo, PageDto pageRequest, Class<T> responseClass);

    // 不联表分页查询
    <T> Page<T> page(CustomQuery customQuery, PageDto pageRequest, Class<T> entityClass);

    Boolean deleteFieldById(String id, String fieldName, Class<?> entityClass);

    Boolean deleteFieldByIds(List<String> ids, String fieldName, Class<?> entityClass);

    Boolean updateFieldById(String id, Map<Field, Object> updateFields, Class<?> entityClass);

    Boolean updateFieldByIds(List<String> ids, Map<Field, Object> updateFields, Class<?> entityClass);

    Boolean updateFieldByIds(List<String> ids, UpdateRequest<?> updateRequest, Class<?> entityClass);

    Boolean updateField(Query query, UpdateDefinition update, Class<?> entityClass);

    <T> List<T> findIncludeFieldByIds(List<String> ids, String collectionName, List<String> filedList,
        Class<T> responseClass);
}
