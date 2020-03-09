package com.sms.satp.parser;

import com.sms.satp.ApplicationTests;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.parser.model.ApiDocument;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApplicationTests.class)
public class DocumentFactoryTest {

    public static final String CONFIG_OPEN_API_V_3_YAML = "/config/openapi_v3.yaml";
    public static final String SWAGGER_V2_ONE_JSON = "/config/swagger-v2-one.json";

    @Autowired
    private DocumentFactory documentFactory;

    @Test
    @DisplayName("Parse api configuration from a file or url")
    public void parser_swagger_document_file() {
        String location = DocumentFactoryTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).toString();
        ApiDocument apiDocument = documentFactory.buildByResource(location, DocumentType.SWAGGER);
        Assertions.assertNotNull(apiDocument);

    }

    @Test
    @DisplayName("Parse api configuration from content")
    public void parser_swagger_document_contents() throws IOException {
        String contents = IOUtils
            .toString(OpenApiDocumentTransformerTest.class.getResource(SWAGGER_V2_ONE_JSON), StandardCharsets.UTF_8);
        ApiDocument apiDocument = documentFactory.buildByContents(contents, DocumentType.SWAGGER);
        Assertions.assertNotNull(apiDocument);

    }


}
