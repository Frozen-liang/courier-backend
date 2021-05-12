package com.sms.satp.service;

import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentResponse findById(String id);

    Boolean add(GlobalEnvironmentRequest globalEnvironmentRequest);

    Boolean edit(GlobalEnvironmentRequest globalEnvironmentRequest);

    List<GlobalEnvironmentResponse> list();

    Boolean delete(String[] ids);

}
