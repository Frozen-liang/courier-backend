package com.sms.courier.parser.impl;

import static com.sms.courier.common.exception.ErrorCode.PARSE_SWAGGER_FILE_ERROR;

import com.sms.courier.parser.DocumentReader;
import com.sms.courier.parser.common.DocumentDefinition;
import com.sms.courier.utils.ExceptionUtils;
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
            throw ExceptionUtils.mpe(PARSE_SWAGGER_FILE_ERROR);
        }
        return DocumentDefinition.builder().document(openApi).build();
    }

}