package com.sms.courier.service;

import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import java.util.List;

public interface DataStructureService {

    DataStructureResponse findById(String id);

    Boolean add(DataStructureRequest dataStructureRequest);

    Boolean edit(DataStructureRequest dataStructureRequest);

    Boolean delete(List<String> ids);

    List<DataStructureListResponse> getDataStructureList(DataStructureListRequest request);

    List<DataStructureResponse> getDataStructureDataList(DataStructureListRequest request);

    List<DataStructureReferenceResponse> getReference(String id);
}
