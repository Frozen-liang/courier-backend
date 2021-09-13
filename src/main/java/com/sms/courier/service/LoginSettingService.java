package com.sms.courier.service;

import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import java.util.List;

public interface LoginSettingService {

    LoginSettingResponse findById(String id);

    List<LoginSettingResponse> list();

    Boolean add(LoginSettingRequest loginSettingRequest);

    Boolean edit(LoginSettingRequest loginSettingRequest);

    Boolean delete(List<String> ids);
}
