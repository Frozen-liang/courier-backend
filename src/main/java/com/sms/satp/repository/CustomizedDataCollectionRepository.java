package com.sms.satp.repository;

import java.util.List;

public interface CustomizedDataCollectionRepository {

    List<String> getParamListById(String id);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);
}
