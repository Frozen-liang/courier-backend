package com.sms.satp.parser.common;

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

public enum DocumentType {
    SWAGGER(0, SwaggerApiDocumentTransformer.INSTANCE, SwaggerDocumentReader.INSTANCE),
    POSTMAN(1, null, null);


    private static final Map<Integer, DocumentType> MAPPINGS =
        Arrays.stream(values()).collect(Collectors.toMap(DocumentType::getType, Function.identity()));


    private final int type;
    private final ApiDocumentTransformer<DocumentParserResult> transformer;
    private final DocumentReader<DocumentParserResult> reader;

    DocumentType(int type, ApiDocumentTransformer<DocumentParserResult> transformer,
        DocumentReader<DocumentParserResult> reader) {

        this.type = type;
        this.transformer = transformer;
        this.reader = reader;
    }


    public int getType() {
        return type;
    }

    public ApiDocumentTransformer<DocumentParserResult> getTransformer() {
        return transformer;
    }

    public DocumentReader<DocumentParserResult> getReader() {
        return reader;
    }

    @NonNull
    public static DocumentType resolve(@Nullable Integer documentType) {
        return MAPPINGS.getOrDefault(documentType, null);
    }


    public boolean matches(Integer documentType) {
        return (this == resolve(documentType));
    }
}
