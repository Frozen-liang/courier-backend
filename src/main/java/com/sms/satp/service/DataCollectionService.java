package com.sms.satp.service;

import com.sms.satp.dto.request.DataCollectionImportRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.response.DataCollectionResponse;
import com.sms.satp.entity.datacollection.DataCollectionEntity;
import java.util.List;

public interface DataCollectionService {

    DataCollectionResponse findById(String id);

    DataCollectionEntity findOne(String id);

    List<DataCollectionResponse> list(String projectId, String collectionName);

    Boolean add(DataCollectionRequest dataCollectionRequest);

    Boolean edit(DataCollectionRequest dataCollectionRequest);

    Boolean delete(List<String> ids);

    List<String> getParamListById(String id);

    Boolean importDataCollection(DataCollectionImportRequest request);
}