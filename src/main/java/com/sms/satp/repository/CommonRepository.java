package com.sms.satp.repository;

import com.sms.satp.common.field.Field;
import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.mongo.LookupVo;
import com.sms.satp.entity.mongo.QueryVo;
import java.util.List;
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

    <T> List<T> list(QueryVo queryVo, Class<T> responseClass);

    <T> List<T> list(String collectionName, LookupVo lookupVo, List<Optional<Criteria>> criteriaList,
        Class<T> responseClass);

    <T> Page<T> page(QueryVo queryVo, PageDto pageRequest, Class<T> responseClass);
}
