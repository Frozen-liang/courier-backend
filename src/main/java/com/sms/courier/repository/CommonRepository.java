package com.sms.courier.repository;

import com.sms.courier.common.field.Field;
import com.sms.courier.dto.PageDto;
import com.sms.courier.entity.mongo.LookupVo;
import com.sms.courier.entity.mongo.QueryVo;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;

public interface CommonRepository {

    Boolean deleteById(String id, Class<?> entityClass);

    Boolean deleteByIds(List<String> ids, Class<?> entityClass);

    Boolean removeTags(Field filed, List<String> tagIds, Class<?> entityClass);

    Boolean recover(List<String> ids, Class<?> entityClass);

    <T> Optional<T> findById(String id, String collectionName, LookupVo lookupVo, Class<T> responseClass);

    <T> Optional<T> findById(String id, String collectionName, List<LookupVo> lookupVo, Class<T> responseClass);

    <T> List<T> listLookupUser(String collectionName, List<Optional<Criteria>> criteriaList, Class<T> responseClass);

    <T> List<T> list(QueryVo queryVo, Class<T> responseClass);

    <T> List<T> list(String collectionName, LookupVo lookupVo, List<Optional<Criteria>> criteriaList,
        Class<T> responseClass);

    <T> Page<T> page(QueryVo queryVo, PageDto pageRequest, Class<T> responseClass);

    Boolean deleteFieldById(String id, String fieldName, Class<?> entityClass);

    Boolean deleteFieldByIds(List<String> ids, String fieldName, Class<?> entityClass);

    Boolean updateFieldById(String id, Map<Field, Object> updateFields, Class<?> entityClass);
}
