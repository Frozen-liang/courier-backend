package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.OperationType;
import com.sms.courier.entity.function.GlobalFunctionEntity;
import java.util.List;
import lombok.Getter;

@Getter
public class EngineGlobalFunctionEvent {

    private final List<GlobalFunctionEntity> functions;
    private final OperationType operationType;

    public EngineGlobalFunctionEvent(List<GlobalFunctionEntity> functions, OperationType operationType) {
        this.functions = functions;
        this.operationType = operationType;
    }
}
