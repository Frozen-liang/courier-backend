package com.sms.courier.engine;

import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.grpc.api.v1.GrpcEngineRegisterRequest;
import com.sms.courier.engine.model.EngineAddress;
import com.sms.courier.engine.request.EngineMemberRequest;
import java.util.List;

public interface EngineMemberManagement {

    void registerEngine(GrpcEngineRegisterRequest request);

    List<EngineResponse> getRunningEngine();

    Boolean openEngine(String id);

    Boolean closeEngine(String id);

    Boolean createEngine();

    Boolean restartEngine(String name);

    Boolean deleteEngine(String name);

    Boolean queryLog(DockerLogRequest request);

    Boolean edit(EngineMemberRequest request);

    Boolean batchDeleteEngine(List<String> names);

    List<EngineAddress> getAvailableEngine();

}
