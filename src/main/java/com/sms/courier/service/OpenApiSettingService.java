package com.sms.courier.service;

import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import java.util.List;

public interface OpenApiSettingService {

    OpenApiSettingResponse findById(String id);

    List<OpenApiSettingResponse> list();

    Boolean add(OpenApiSettingRequest openApiSettingRequest);

    Boolean edit(OpenApiSettingRequest openApiSettingRequest);

    Boolean delete(List<String> ids);

    Boolean enable(List<String> ids);

    Boolean unable(List<String> ids);
}
