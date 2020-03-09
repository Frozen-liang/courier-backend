package com.sms.satp.parser;

import com.sms.satp.parser.impl.OpenApiDocumentTransformer;
import com.sms.satp.parser.model.ApiInfo;
import com.sms.satp.parser.model.ApiPath;
import com.sms.satp.parser.model.ApiTag;
import com.sms.satp.parser.schema.ApiSchema;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.ParseOptions;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OpenApiDocumentTransformerTest {


    public static final String CONFIG_OPEN_API_V_3_YAML = "/config/openapi_v3.yaml";
    public static final String SWAGGER_V2_ONE_JSON = "/config/swagger-v2-one.json";

    @Test
    public void parser_open_api_v3_normal() {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        OpenAPI openAPI = new OpenAPIParser()
            .readLocation(OpenApiDocumentTransformerTest.class.getResource(CONFIG_OPEN_API_V_3_YAML).toString(),
                null,
                parseOptions).getOpenAPI();
        OpenApiDocumentTransformer openApiDocumentTransform = new OpenApiDocumentTransformer();
        ApiInfo apiInfo = openApiDocumentTransform.transformInfo(openAPI);
        List<ApiPath> apiPaths = openApiDocumentTransform.transformPaths(openAPI);
        Map<String, ApiSchema> apiSchema = openApiDocumentTransform.transformSchemas(openAPI);
        List<ApiTag> apiTags = openApiDocumentTransform.transformTags(openAPI);
        Assertions.assertSame(openAPI.getInfo().getTitle(), apiInfo.getTitle());
        Assertions.assertEquals(openAPI.getPaths().size(), apiPaths.size());
        Assertions.assertEquals(openAPI.getComponents().getSchemas().size(), apiSchema.size());
        Assertions.assertEquals(openAPI.getTags().size(), apiTags.size());
    }

    @Test
    public void parser_swagger_v2_normal() throws IOException {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        String contents = IOUtils
            .toString(OpenApiDocumentTransformerTest.class.getResource(SWAGGER_V2_ONE_JSON), StandardCharsets.UTF_8);
        OpenAPI openAPI = new OpenAPIParser()
            .readContents(contents, null,
                parseOptions).getOpenAPI();

        OpenApiDocumentTransformer openApiDocumentTransform = new OpenApiDocumentTransformer();
        ApiInfo apiInfo = openApiDocumentTransform.transformInfo(openAPI);
        List<ApiPath> apiPaths = openApiDocumentTransform.transformPaths(openAPI);
        Map<String, ApiSchema> apiSchema = openApiDocumentTransform.transformSchemas(openAPI);
        List<ApiTag> apiTags = openApiDocumentTransform.transformTags(openAPI);
        Assertions.assertSame(openAPI.getInfo().getTitle(), apiInfo.getTitle());
        Assertions.assertEquals(openAPI.getPaths().size(), apiPaths.size());
        Assertions.assertEquals(openAPI.getComponents().getSchemas().size(), apiSchema.size());
        Assertions.assertEquals(CollectionUtils.isEmpty(openAPI.getTags()), CollectionUtils.isEmpty(apiTags));
    }
}
