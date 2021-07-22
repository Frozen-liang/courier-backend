package com.sms.satp.engine;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.CaseRecordRequest;
import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.request.EngineRegistrationRequest;

public interface EngineMemberManagement {


    String bind(EngineRegistrationRequest request);

    void unBind(String sessionId);

    void active(String sessionId, String destination);

    void updateMemberStatus(String destination, EngineStatus status);

    String getAvailableMember() throws ApiTestPlatformException;

    void caseRecord(CaseRecordRequest caseRecordRequest);
}
