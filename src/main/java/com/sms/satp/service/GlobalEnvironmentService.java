package com.sms.satp.service;

import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironment;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentResponse findById(String id);

    GlobalEnvironment findOne(String id);

    Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest);

    Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest);

    List<GlobalEnvironmentResponse> list();

    Boolean delete(List<String> ids);

}
