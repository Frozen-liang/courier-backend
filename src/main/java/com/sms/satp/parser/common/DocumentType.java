package com.sms.satp.parser.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum DocumentType {
    SWAGGER,
    POSTMAN;

    private static final Map<String, DocumentType> mappings = new HashMap<>(16);

    static {
        for (DocumentType documentType : values()) {
            mappings.put(documentType.name(), documentType);
        }
    }


    @Nullable
    public static DocumentType resolve(@Nullable String documentType) {
        return (documentType != null ? mappings.get(documentType) : null);
    }


    public boolean matches(String documentType) {
        return (this == resolve(documentType));
    }
}
