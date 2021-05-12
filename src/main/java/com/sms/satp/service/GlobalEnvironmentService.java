package com.sms.satp.service;

import com.sms.satp.dto.GlobalEnvironmentRequest;
import com.sms.satp.dto.GlobalEnvironmentResponse;
import java.util.List;

public interface GlobalEnvironmentService {

    GlobalEnvironmentResponse findById(String id);

    void add(GlobalEnvironmentRequest globalEnvironmentRequest);

    void edit(GlobalEnvironmentRequest globalEnvironmentRequest);

    List<GlobalEnvironmentResponse> list();

    void delete(String[] ids);

}
