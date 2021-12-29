package com.sms.courier.service;

import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import java.util.List;

public interface AuthSettingService {

    AuthSettingResponse findById(String id);

    List<AuthSettingResponse> list();

    Boolean add(AuthSettingRequest authSettingRequest);

    Boolean edit(AuthSettingRequest authSettingRequest);

    Boolean delete(String id);
}
