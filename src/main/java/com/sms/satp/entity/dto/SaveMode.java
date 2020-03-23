package com.sms.satp.entity.dto;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum SaveMode {
    COVER(),
    REMAIN();

    private static final Map<String, SaveMode> mappings = new HashMap<>(16);

    static {
        for (SaveMode saveMode : values()) {
            mappings.put(saveMode.name(), saveMode);
        }
    }

    @Nullable
    public static SaveMode resolve(@Nullable String saveMode) {
        return (saveMode != null ? mappings.get(saveMode) : null);
    }


    public boolean matches(String saveMode) {
        return (this == resolve(saveMode));
    }
}
