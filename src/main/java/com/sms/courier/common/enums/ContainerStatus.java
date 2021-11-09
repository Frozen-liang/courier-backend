package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public enum ContainerStatus implements EnumCommon {

    START("start", 0),
    DIE("die", 1),
    DESTROY("destroy", 2);

    private final String status;
    private final int code;

    ContainerStatus(String status, int code) {
        this.status = status;
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.status;
    }

    private static final Map<Integer, ContainerStatus> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(ContainerStatus::getCode, Function.identity()));

    private static final Map<String, ContainerStatus> CONTAINER_STATUS_MAP = Arrays.stream(values()).sequential()
        .collect(Collectors.toMap(ContainerStatus::getName, Function.identity()));

    @Nullable
    public static ContainerStatus getType(@Nullable Integer code) {
        return (code != null ? MAPPINGS.get(code) : null);
    }

    @Nullable
    public static ContainerStatus resolverByName(@Nullable String name) {
        return CONTAINER_STATUS_MAP.get(name);
    }

}
