package com.sms.satp.parser;

import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.parser.model.ApiDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DocumentFactoryApplicationTests.class)
public class DocumentFactoryTest {
    public static final String CONFIG_OPEN_API_V_3_YAML = "/config/openapi_v3.yaml";
    @Autowired
    private DocumentFactory documentFactory;

    @Test
    public void parser_swagger_document_normal(){
        String location = DocumentFactoryTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).toString();
        ApiDocument apiDocument = documentFactory.create(location, DocumentType.SWAGGER);
        Assertions.assertNotNull(apiDocument);

    }
}
