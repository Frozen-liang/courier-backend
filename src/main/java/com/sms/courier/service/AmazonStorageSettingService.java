package com.sms.courier.service;

import com.sms.courier.dto.request.AmazonStorageSettingRequest;
import com.sms.courier.dto.response.AmazonStorageSettingResponse;

public interface AmazonStorageSettingService {

    AmazonStorageSettingResponse findOne();

    Boolean add(AmazonStorageSettingRequest amazonStorageSettingRequest);

    Boolean edit(AmazonStorageSettingRequest amazonStorageSettingRequest);

    Boolean delete(String id);
}
