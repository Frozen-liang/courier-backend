package com.sms.satp.parser.impl;

import com.sms.satp.parser.DocumentReader;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.utils.ExceptionUtils;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwaggerFileDocumentReader implements DocumentReader {

    private static final ParseOptions PARSE_OPTIONS = new ParseOptions();

    static {
        PARSE_OPTIONS.setResolve(true);
    }

    public DocumentDefinition<?> read(String content) {
        OpenAPI openApi = null;
        try {
            openApi = new OpenAPIParser()
                .readContents(content,
                    null,
                    PARSE_OPTIONS).getOpenAPI();
        } catch (Exception e) {
            log.error("Parse swagger file error. message:{}", e.getMessage());
            throw ExceptionUtils.mpe("Parse the swagger file error, Please check the format of the file contents.");
        }
        return DocumentDefinition.builder().document(openApi).build();
    }

}