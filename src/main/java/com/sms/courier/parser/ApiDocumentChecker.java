package com.sms.courier.parser;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.entity.api.ApiEntity;
import java.util.List;

public interface ApiDocumentChecker {

    void check(List<ApiEntity> waitApiEntities) throws ApiTestPlatformException;
}