package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum DatabaseType implements EnumCommon {

    MYSQL(0);

    private static final Map<Integer, DatabaseType> mappings = new HashMap<>(16);

    static {
        for (DatabaseType dataBaseType : values()) {
            mappings.put(dataBaseType.getCode(), dataBaseType);
        }
    }

    private Integer code;

    DatabaseType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static DatabaseType getDatabaseType(Integer code) {
        return mappings.get(code);
    }
}
