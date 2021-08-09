package com.sms.courier.engine;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.engine.request.EngineRegistrationRequest;

public interface EngineMemberManagement {

    String bind(EngineRegistrationRequest request);

    void unBind(String sessionId);

    void active(String sessionId, String destination);

    String getAvailableMember() throws ApiTestPlatformException;

    void caseRecord(CaseRecordRequest caseRecordRequest);

    void countTaskRecord(String destination, Integer size);
}
