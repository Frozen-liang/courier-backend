package com.sms.satp.parser.impl;

import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.utils.ExceptionUtils;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwaggerUrlDocumentReader implements DocumentReader {

    private static final ParseOptions PARSE_OPTIONS = new ParseOptions();

    static {
        PARSE_OPTIONS.setResolve(true);
    }

    public DocumentDefinition<?> read(String url) {
        OpenAPI openApi = null;
        try {
            openApi = new OpenAPIParser()
                .readLocation(url,
                    null,
                    PARSE_OPTIONS).getOpenAPI();
        } catch (Exception e) {
            log.error("Parse the swagger url error. message:{}", e.getMessage());
            throw ExceptionUtils.mpe("Parse the swagger url error, Please check the url.");
        }
        return DocumentDefinition.builder().document(openApi).build();
    }

}