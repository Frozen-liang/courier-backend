package com.sms.satp.engine;

import com.sms.satp.dto.request.CaseRecordRequest;
import com.sms.satp.engine.enums.EngineStatus;
import com.sms.satp.engine.model.EngineMember;
import com.sms.satp.engine.request.EngineRegistrationRequest;
import java.util.Set;

public interface EngineMemberManagement {


    String bind(EngineRegistrationRequest request);

    void unBind(String sessionId);

    void active(EngineMember engineMember);

    void updateMemberStatus(String destination, EngineStatus status);

    Set<String> getAvailableMembers();

    void caseRecord(CaseRecordRequest caseRecordRequest);
}
