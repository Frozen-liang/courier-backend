package com.sms.courier.service;

import com.sms.courier.dto.request.DataStructureRequest;
import com.sms.courier.dto.response.DataStructureResponse;
import java.util.List;

public interface DataStructureService {

    DataStructureResponse findById(String id);

    List<DataStructureResponse> list();

    Boolean add(DataStructureRequest dataStructureRequest);

    Boolean edit(DataStructureRequest dataStructureRequest);

    Boolean delete(List<String> ids);
}
