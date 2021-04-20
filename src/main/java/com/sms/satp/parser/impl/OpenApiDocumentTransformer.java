package com.sms.satp.parser.impl;

import static com.sms.satp.common.ErrorCode.PARSER_OPEN_API_ERROR;
import static com.sms.satp.parser.OpenApiConverterFunction.INFO_CONVERTER;
import static com.sms.satp.parser.OpenApiConverterFunction.OPERATION_CONVERTER;
import static com.sms.satp.parser.OpenApiConverterFunction.PROPERTIES_CONVERTER;
import static com.sms.satp.parser.OpenApiConverterFunction.TAGS_CONVERTER;
import static java.util.stream.Collectors.toList;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.parser.DocumentTransformer;
import com.sms.satp.parser.annotation.Reader;
import com.sms.satp.parser.common.DocumentType;
import com.sms.satp.parser.model.ApiInfo;
import com.sms.satp.parser.model.ApiOperation;
import com.sms.satp.parser.model.ApiPath;
import com.sms.satp.parser.model.ApiTag;
import com.sms.satp.parser.schema.ApiSchema;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;


@Reader(documentType = DocumentType.SWAGGER)
@Slf4j
public class OpenApiDocumentTransformer implements DocumentTransformer<OpenAPI> {

    private static final OpenAPIParser OPEN_API_PARSER = new OpenAPIParser();

    @Override
    public ApiInfo transformInfo(OpenAPI openApi) {
        ApiInfo apiInfo = INFO_CONVERTER.apply(openApi.getInfo());
        if (Objects.isNull(apiInfo)) {
            log.warn("Document of OpenApi parsing has failed");
            throw new ApiTestPlatformException(PARSER_OPEN_API_ERROR);
        }
        log.info("Parsing [OpenApi Info] content:{}", apiInfo);
        return apiInfo;
    }

    @Override
    public List<ApiTag> transformTags(OpenAPI openApi) {
        List<ApiTag> apiTags = TAGS_CONVERTER.apply(openApi.getTags());
        log.info("Parsing [OpenApi Tags] content:{}", apiTags);
        return apiTags;
    }

    @Override
    public List<ApiPath> transformPaths(OpenAPI openApi) {
        Paths paths = openApi.getPaths();
        if (MapUtils.isEmpty(paths)) {
            log.warn("Document of OpenApi parsing has failed");
            throw new ApiTestPlatformException(PARSER_OPEN_API_ERROR);
        }
        List<ApiPath> apiPaths = new ArrayList<>(paths.entrySet().size());
        for (Map.Entry<String, PathItem> pathItemEntry : paths.entrySet()) {
            List<ApiOperation> apiOperations = collectApiOperations(pathItemEntry);
            ApiPath apiPath = ApiPath
                .builder()
                .path(pathItemEntry.getKey()).summary(pathItemEntry.getValue().getSummary())
                .description(pathItemEntry.getValue().getDescription()).operations(apiOperations).build();
            apiPaths.add(apiPath);

        }
        return apiPaths;
    }

    private List<ApiOperation> collectApiOperations(Entry<String, PathItem> pathItemEntry) {
        PathItem pathItem = pathItemEntry.getValue();
        return Arrays.stream(RequestMethod.values())
            .map(httpMethod -> StringUtils.capitalize(httpMethod.name().toLowerCase(Locale.US))).map(methodName -> {
                try {
                    Operation operation = (Operation) MethodUtils.invokeMethod(pathItem, "get".concat(methodName));
                    if (Objects.nonNull(operation)) {

                        return OPERATION_CONVERTER.apply(operation)
                            .requestMethod(RequestMethod.resolve(StringUtils.upperCase(methodName, Locale.US))).build();
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.warn("Parsing [OpenApi {} Operation] content,Cause by {}", methodName, e.getMessage());
                }
                return null;
            }).filter(Objects::nonNull)
            .collect(toList());
    }

    @Override
    public Map<String, ApiSchema> transformSchemas(OpenAPI openApi) {
        Components components = openApi.getComponents();
        if (Objects.nonNull(components)) {
            return PROPERTIES_CONVERTER.apply(components.getSchemas());
        }
        return new HashMap<>();
    }

    @Override
    public OpenAPI readLocation(String location) {
        try {
            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);
            SwaggerParseResult swaggerParseResult = OPEN_API_PARSER.readLocation(location, null, parseOptions);
            verifyResult(swaggerParseResult);
            return swaggerParseResult.getOpenAPI();
        } catch (Exception e) {
            return handleError(e);
        }
    }


    @Override
    public OpenAPI readContents(String contents) {
        try {
            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);
            SwaggerParseResult swaggerParseResult = OPEN_API_PARSER.readContents(contents, null, parseOptions);
            verifyResult(swaggerParseResult);
            return swaggerParseResult.getOpenAPI();
        } catch (Exception e) {
            return handleError(e);
        }
    }

    private void verifyResult(SwaggerParseResult swaggerParseResult) {
        if (CollectionUtils.isNotEmpty(swaggerParseResult.getMessages())) {
            log.warn("API file has been parsed to fail error={}", swaggerParseResult.getMessages());
        }
    }

    private OpenAPI handleError(Exception e) {
        log.error("Failed to read the [OpenAPI location]", e);
        throw new ApiTestPlatformException(ErrorCode.FILE_FORMAT_ERROR);
    }
}
