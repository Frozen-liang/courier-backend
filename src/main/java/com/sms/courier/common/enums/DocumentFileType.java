package com.sms.courier.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum DocumentFileType {

    SWAGGER_FILE(0, DocumentType.SWAGGER_FILE);

    private final int code;
    private final DocumentType documentType;


    DocumentFileType(int code, DocumentType documentType) {
        this.code = code;
        this.documentType = documentType;
    }

    private static final Map<Integer, DocumentFileType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(DocumentFileType::getCode, Function.identity()));

    public int getCode() {
        return code;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }


    @NonNull
    public static DocumentFileType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }

}
