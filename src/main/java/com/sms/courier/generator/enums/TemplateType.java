package com.sms.courier.generator.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.HashMap;
import java.util.Map;

public enum TemplateType implements EnumCommon {

    ENTITY(0), CONTROLLER(1), SERVICE(2), SERVICE_IMPL(3);

    private final Integer code;

    TemplateType(Integer code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
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
