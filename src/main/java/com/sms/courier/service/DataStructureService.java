package com.sms.courier.service;

import com.sms.courier.dto.request.DataStructureListRequest;
import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureListResponse;
import com.sms.courier.dto.response.DataStructureReferenceResponse;
import com.sms.courier.dto.response.DataStructureResponse;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface DataStructureService {

    DataStructureResponse findById(String id);

    Boolean add(DataStructureRequest dataStructureRequest);

    Boolean edit(DataStructureRequest dataStructureRequest);

    Boolean delete(String ids);

    Page<DataStructureListResponse> getDataStructureList(DataStructureListRequest request);

    List<DataStructureResponse> getDataStructureDataList(DataStructureListRequest request);

    Map<String, List<DataStructureReferenceResponse>> getReference(String id);

    List<DataStructureResponse> getRefStructList(DataStructureListRequest request);
}
