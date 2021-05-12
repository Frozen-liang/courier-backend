package com.sms.satp.service;

import com.sms.satp.dto.DataCollectionRequest;
import com.sms.satp.dto.DataCollectionResponse;
import java.util.List;

public interface DataCollectionService {

    DataCollectionResponse findById(String id);

    List<DataCollectionResponse> list(String projectId, String collectionName);

    void add(DataCollectionRequest dataCollectionRequest);

    void edit(DataCollectionRequest dataCollectionRequest);

    void delete(String[] ids);

    List<String> getParamListById(String id);
}