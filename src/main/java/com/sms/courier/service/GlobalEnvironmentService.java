package com.sms.courier.service;

import com.sms.courier.dto.request.GlobalEnvironmentRequest;
import com.sms.courier.dto.response.GlobalEnvironmentResponse;
import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentResponse findById(String id);

    GlobalEnvironmentEntity findOne(String id);

    Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest);

    Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest);

    List<GlobalEnvironmentResponse> list(String workspaceId);

    Boolean delete(List<String> ids);

}
