package com.sms.courier.service;

import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import java.util.List;

public interface OAuthSettingService {

    OAuthSettingResponse findById(String id);

    List<OAuthSettingResponse> list();

    Boolean add(OAuthSettingRequest authSettingRequest);

    Boolean edit(OAuthSettingRequest authSettingRequest);

    Boolean delete(String id);
}
