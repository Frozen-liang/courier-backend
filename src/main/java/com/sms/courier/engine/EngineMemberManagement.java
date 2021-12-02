package com.sms.courier.engine;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.request.EngineMemberRequest;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import java.util.List;

public interface EngineMemberManagement {

    String bind(EngineRegistrationRequest request);

    void unBind(String sessionId);

    void active(String sessionId, String destination);

    String getAvailableMember() throws ApiTestPlatformException;

    void caseRecord(CaseRecordRequest caseRecordRequest);

    void countTaskRecord(String destination, Integer size);

    List<EngineResponse> getRunningEngine();

    Boolean openEngine(String id);

    Boolean closeEngine(String id);

    Boolean createEngine();

    Boolean restartEngine(String name);

    Boolean deleteEngine(String name);

    Boolean queryLog(DockerLogRequest request);

    Boolean edit(EngineMemberRequest request);

    Boolean batchDeleteEngine(List<String> names);
}
