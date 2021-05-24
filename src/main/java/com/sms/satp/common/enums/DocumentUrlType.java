package com.sms.satp.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum DocumentUrlType {

    SWAGGER_FILE(0, DocumentType.SWAGGER_URL);

    private final int code;
    private final DocumentType documentType;

    private static final Map<Integer, DocumentUrlType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(DocumentUrlType::getCode, Function.identity()));

    DocumentUrlType(int code, DocumentType documentType) {
        this.code = code;
        this.documentType = documentType;
    }

    public int getCode() {
        return code;
    }

    public DocumentType getDocumentType() {

        return documentType;
    }

    @NonNull
    public static DocumentUrlType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }

}
