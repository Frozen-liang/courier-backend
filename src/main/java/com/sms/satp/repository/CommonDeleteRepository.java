package com.sms.satp.repository;

import com.sms.satp.common.field.Filed;
import com.sms.satp.entity.api.ApiEntity;
import java.util.List;

public interface CommonDeleteRepository {

    Boolean deleteById(String id, Class<?> entityClass);

    Boolean deleteByIds(List<String> ids, Class<?> entityClass);

    Boolean removeTags(Filed filed, List<String> tagIds, Class<?> entityClass);

    Boolean recover(List<String> ids, Class<?> entityClass);
}
