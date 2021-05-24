package com.sms.satp.parser.impl;

import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;

public class SwaggerUrlDocumentReader implements DocumentReader {

    private static final ParseOptions PARSE_OPTIONS = new ParseOptions();

    static {
        PARSE_OPTIONS.setResolve(true);
    }

    public DocumentDefinition<?> read(String url) {
        OpenAPI openApi = new OpenAPIParser()
            .readLocation(url,
                null,
                PARSE_OPTIONS).getOpenAPI();
        return DocumentDefinition.builder().document(openApi).build();
    }

}