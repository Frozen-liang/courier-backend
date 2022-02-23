package com.sms.courier.service;

import com.sms.courier.dto.request.DataCollectionImportRequest;
import com.sms.courier.dto.request.DataCollectionRequest;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.dto.response.ExportExcelResponse;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import java.util.List;

public interface DataCollectionService {

    DataCollectionResponse findById(String id);

    DataCollectionEntity findOne(String id);

    List<DataCollectionResponse> list(String projectId, String collectionName, String groupId);

    Boolean add(DataCollectionRequest dataCollectionRequest);

    Boolean edit(DataCollectionRequest dataCollectionRequest);

    Boolean delete(List<String> ids);

    List<String> getParamListById(String id);

    Boolean importDataCollection(DataCollectionImportRequest request);

    List<DataCollectionResponse> listByEnvIdAndEnvIdIsNull(String envId, String projectId);

    ExportExcelResponse export(String id);
}