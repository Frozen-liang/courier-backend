package com.sms.satp.common.enums;

import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.impl.SwaggerApiDocumentTransformer;
import com.sms.satp.parser.impl.SwaggerDocumentReader;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum DocumentType implements EnumCommon {
    SWAGGER(0, SwaggerApiDocumentTransformer.INSTANCE, SwaggerDocumentReader.INSTANCE),
    POSTMAN(1, null, null);


    private static final Map<Integer, DocumentType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(DocumentType::getCode, Function.identity()));


    private final int code;
    private final ApiDocumentTransformer transformer;
    private final DocumentReader reader;

    DocumentType(int code, ApiDocumentTransformer transformer,
        DocumentReader reader) {

        this.code = code;
        this.transformer = transformer;
        this.reader = reader;
    }


    public int getCode() {
        return code;
    }

    public ApiDocumentTransformer getTransformer() {
        return transformer;
    }

    public DocumentReader getReader() {
        return reader;
    }

    @NonNull
    public static DocumentType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }


    public boolean matches(Integer code) {
        return (this == getType(code));
    }
}
