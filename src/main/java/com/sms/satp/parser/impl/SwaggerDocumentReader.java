package com.sms.satp.parser.impl;

import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;

public enum SwaggerDocumentReader implements DocumentReader {
    INSTANCE;
    private static final ParseOptions PARSE_OPTIONS = new ParseOptions();

    static {
        PARSE_OPTIONS.setResolve(true);
    }

    @Override
    public DocumentDefinition<?> readLocation(String location, String projectId) {
        OpenAPI openApi = new OpenAPIParser()
            .readLocation(location,
                null,
                PARSE_OPTIONS).getOpenAPI();
        return DocumentDefinition.builder().document(openApi).build();
    }

    @Override
    public DocumentDefinition<?> readContents(String content, String projectId) {
        OpenAPI openApi = new OpenAPIParser()
            .readContents(content,
                null,
                PARSE_OPTIONS).getOpenAPI();
        return DocumentDefinition.builder().document(openApi).build();
    }
}