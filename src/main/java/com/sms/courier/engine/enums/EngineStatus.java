package com.sms.courier.engine.enums;

import static io.grpc.ConnectivityState.CONNECTING;
import static io.grpc.ConnectivityState.IDLE;
import static io.grpc.ConnectivityState.READY;
import static io.grpc.ConnectivityState.TRANSIENT_FAILURE;

import com.sms.courier.common.enums.EnumCommon;
import io.grpc.ConnectivityState;
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

    public static EngineStatus getByConnectivityState(ConnectivityState state) {
        if (READY == state) {
            return EngineStatus.RUNNING;
        } else if (state == CONNECTING) {
            return EngineStatus.PENDING;
        } else if (state == TRANSIENT_FAILURE || state == IDLE) {
            return EngineStatus.WAITING_FOR_RECONNECTION;
        } else {
            return EngineStatus.INVALID;
        }
    }
}
