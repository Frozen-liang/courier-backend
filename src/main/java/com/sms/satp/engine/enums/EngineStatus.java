package com.sms.satp.engine.enums;

import com.sms.satp.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EngineStatus implements EnumCommon {
    PENDING(0),
    RUNNING(1),
    WAITING_FOR_RECONNECTION(2),
    INVALID(3);

    EngineStatus(Integer code) {
        this.code = code;
    }

    private final Integer code;
    private static final Map<Integer, EngineStatus> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(EngineStatus::getCode, Function.identity()));

    @Override
    public int getCode() {
        return this.code;
    }

    public static EngineStatus getType(Integer code) {
        return MAPPINGS.get(code);
    }
}
