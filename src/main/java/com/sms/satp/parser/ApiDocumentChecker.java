package com.sms.satp.parser;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.entity.api.ApiEntity;
import java.util.List;

public interface ApiDocumentChecker {

    void check(List<ApiEntity> waitApiEntities) throws ApiTestPlatformException;
}