package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum ScheduleStatusType implements EnumCommon {

    CREATE(0), UPDATE(1), DELETE(2);

    private static final Map<Integer, ScheduleStatusType> mappings = new HashMap<>(16);

    static {
        for (ScheduleStatusType scheduleStatusType : values()) {
            mappings.put(scheduleStatusType.getCode(), scheduleStatusType);
        }
    }

    private final Integer code;

    ScheduleStatusType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static ScheduleStatusType getType(Integer code) {
        return mappings.get(code);
    }
}
