package com.sms.satp.parser;

import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.parser.impl.SwaggerApiDocumentTransformer;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OpenApiDocumentTransformerTest {


    @ParameterizedTest
    @ValueSource(strings = {"/config/openapi_v3.yaml", "/config/swagger-v2-one.json", "/config/swagger_java.json"})
    public void parser_normal(String candidate) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        OpenAPI openAPI = new OpenAPIParser()
            .readLocation(
                Objects.requireNonNull(OpenApiDocumentTransformerTest.class.getResource(candidate))
                    .toString(),
                null,
                parseOptions).getOpenAPI();
        DocumentDefinition<OpenAPI> documentDefinition = new DocumentDefinition<>(openAPI);
        ApiDocumentTransformer<OpenAPI> swaggerApiDocumentTransformer = new SwaggerApiDocumentTransformer();
        List<ApiEntity> apiEntities = swaggerApiDocumentTransformer
            .toApiEntities(documentDefinition, apiEntity -> {
            });
        Assertions.assertTrue(apiEntities.size() > 0);

    }





    @Disabled
    @Test
    public void parser_swagger_v2_by_url() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        OpenAPI openAPI = new OpenAPIParser()
            .readLocation("https://onedev.smsassist.com/swagger/docs/v1", null, parseOptions).getOpenAPI();
    }

}
