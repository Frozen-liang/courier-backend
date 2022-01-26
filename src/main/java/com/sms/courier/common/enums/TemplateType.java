package com.sms.courier.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum TemplateType implements EnumCommon {

    ENTITY(0, "entity"), CONTROLLER(1, "controller"),
    SERVICE(2, "service"), SERVICE_IMPL(3, "serviceImpl");

    private final Integer code;
    private final String name;

    TemplateType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, TemplateType> mappings = new HashMap<>(16);

    static {
        for (TemplateType templateType : values()) {
            mappings.put(templateType.getCode(), templateType);
        }
    }

    public static TemplateType getTemplateType(Integer code) {
        return mappings.get(code);
    }
}
