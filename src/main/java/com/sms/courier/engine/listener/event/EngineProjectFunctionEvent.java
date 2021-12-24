package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.OperationType;
import com.sms.courier.entity.function.ProjectFunctionEntity;
import java.util.List;
import lombok.Getter;

@Getter
public class EngineProjectFunctionEvent {

    private final List<ProjectFunctionEntity> functions;
    private final OperationType operationType;

    public EngineProjectFunctionEvent(List<ProjectFunctionEntity> functions, OperationType operationType) {
        this.functions = functions;
        this.operationType = operationType;
    }
}
