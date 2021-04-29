package com.sms.satp.parser.impl;

import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentParserResult;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;

public enum SwaggerDocumentReader implements DocumentReader<DocumentParserResult> {
    INSTANCE;
    private final static ParseOptions PARSE_OPTIONS = new ParseOptions();

    static {
        PARSE_OPTIONS.setResolve(true);
    }

    @Override
    public DocumentParserResult readLocation(String location) {
        OpenAPI openAPI = new OpenAPIParser()
            .readLocation(location,
                null,
                PARSE_OPTIONS).getOpenAPI();
        return new DocumentParserResult(openAPI);
    }

    @Override
    public DocumentParserResult readContents(String content) {
        OpenAPI openAPI = new OpenAPIParser()
            .readContents(content,
                null,
                PARSE_OPTIONS).getOpenAPI();
        return new DocumentParserResult(openAPI);
    }
}
