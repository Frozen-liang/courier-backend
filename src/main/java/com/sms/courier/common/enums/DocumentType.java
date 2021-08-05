package com.sms.courier.common.enums;

import com.sms.courier.parser.ApiDocumentChecker;
import com.sms.courier.parser.ApiDocumentTransformer;
import com.sms.courier.parser.DocumentReader;
import com.sms.courier.parser.impl.OperationIdDuplicateChecker;
import com.sms.courier.parser.impl.SwaggerApiDocumentTransformer;
import com.sms.courier.parser.impl.SwaggerFileDocumentReader;
import com.sms.courier.parser.impl.SwaggerUrlDocumentReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public enum DocumentType implements EnumCommon {
    SWAGGER_URL(0, new SwaggerApiDocumentTransformer(), new SwaggerUrlDocumentReader(), List.of(
        new OperationIdDuplicateChecker())),
    SWAGGER_FILE(1, new SwaggerApiDocumentTransformer(), new SwaggerFileDocumentReader(), List.of(
        new OperationIdDuplicateChecker())),
    POSTMAN(2, null, null, Collections.emptyList());


    private static final Map<Integer, DocumentType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(DocumentType::getCode, Function.identity()));


    private final int code;
    private final ApiDocumentTransformer transformer;
    private final DocumentReader reader;
    private final List<ApiDocumentChecker> apiDocumentCheckers;

    DocumentType(int code, ApiDocumentTransformer transformer,
        DocumentReader reader, List<ApiDocumentChecker> apiDocumentCheckers) {

        this.code = code;
        this.transformer = transformer;
        this.reader = reader;
        this.apiDocumentCheckers = apiDocumentCheckers;
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

    public List<ApiDocumentChecker> getApiDocumentCheckers() {
        return apiDocumentCheckers;
    }

    @NonNull
    public static DocumentType getType(@Nullable Integer code) {
        return MAPPINGS.getOrDefault(code, null);
    }


    public boolean matches(Integer code) {
        return (this == getType(code));
    }
}
