package com.sms.satp.service;

import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironmentEntity;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentResponse findById(String id);

    GlobalEnvironmentEntity findOne(String id);

    Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest);

    Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest);

    List<GlobalEnvironmentResponse> list(String workspaceId);

    Boolean delete(List<String> ids);

}
