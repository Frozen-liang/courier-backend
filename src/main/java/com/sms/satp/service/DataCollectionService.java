package com.sms.satp.service;

import com.sms.satp.entity.dto.DataCollectionDto;
import java.util.List;

public interface DataCollectionService {

    DataCollectionDto findById(String id);

    List<DataCollectionDto> list(String projectId, String collectionName);

    void add(DataCollectionDto dataCollectionDto);

    void edit(DataCollectionDto dataCollectionDto);

    void delete(String[] ids);

    List<String> getParamListById(String id);
}