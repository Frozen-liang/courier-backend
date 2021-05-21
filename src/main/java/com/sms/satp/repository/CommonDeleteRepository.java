package com.sms.satp.repository;

import java.util.List;

public interface CommonDeleteRepository {

    Boolean deleteById(String id, Class<?> entityClass);

    Boolean deleteByIds(List<String> ids, Class<?> entityClass);

}
