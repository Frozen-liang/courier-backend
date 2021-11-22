package com.sms.courier.parser.impl;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ApiProtocol;
import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.In;
import com.sms.courier.common.enums.Media;
import com.sms.courier.common.enums.ParamType;
import com.sms.courier.common.enums.RequestMethod;
import com.sms.courier.common.enums.SchemaType;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiEntity.ApiEntityBuilder;
import com.sms.courier.entity.api.common.ParamInfo;
import com.sms.courier.entity.group.ApiGroupEntity;
import com.sms.courier.parser.ApiDocumentTransformer;
import com.sms.courier.parser.common.DocumentDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

@Slf4j
public class SwaggerApiDocumentTransformer implements ApiDocumentTransformer<OpenAPI> {


    private static final String COMPONENT_KEY_PATTERN = "#/components/schemas/";
    public static final String GET = "get";
    public static final String DEFAULT_RESPONSE_KEY = "200";
    private static final Consumer<ApiEntity> EMPTY_API_CALLBACK = (apiEntity) -> {
    };
    private static final Consumer<ApiGroupEntity> EMPTY_GROUP_CALLBACK = (apiGroupEntity) -> {
    };
    public static final String DELIMITER = "_";
    public static final String SPLIT = "/";

    @Override

    public List<ApiEntity> toApiEntities(DocumentDefinition<OpenAPI> definition, Consumer<ApiEntity> callback) {
        OpenAPI definitionDocument = definition.getDocument();
        Map<String, Schema> components = definitionDocument.getComponents().getSchemas();
        return definitionDocument.getPaths().entrySet().stream()
            .map(entry -> buildApiEntities(entry, components, Optional.ofNullable(callback).orElse(EMPTY_API_CALLBACK)))
            .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Set<ApiGroupEntity> toApiGroupEntities(DocumentDefinition<OpenAPI> definition,
        Consumer<ApiGroupEntity> callback) {

        OpenAPI document = definition.getDocument();
        return document.getPaths().entrySet().stream()
            .map(entry -> this.buildGroups(entry, Optional.ofNullable(callback).orElse(EMPTY_GROUP_CALLBACK)))
            .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Set<ApiGroupEntity> buildGroups(Entry<String, PathItem> entry, Consumer<ApiGroupEntity> callback) {

        PathItem item = entry.getValue();
        return Arrays.stream(RequestMethod.values()).sequential()
            .map(method -> convertToTuple(item, method))
            .filter(tuple -> Objects.nonNull(tuple._2))
            .map(Tuple2::_2)
            .map(this::buildApiGroup).filter(Objects::nonNull)
            .peek(callback)
            .collect(Collectors.toSet());

    }

    public ApiGroupEntity buildApiGroup(Operation operation) {
        List<String> tags = operation.getTags();
        return tags.stream().findFirst().map(groupName -> ApiGroupEntity.builder().name(groupName).build())
            .orElse(null);
    }

    private List<ApiEntity> buildApiEntities(Entry<String, PathItem> entry,
        Map<String, Schema> components, Consumer<ApiEntity> callback) {
        PathItem item = entry.getValue();
        String apiPath = entry.getKey();
        return Arrays.stream(RequestMethod.values()).sequential()
            .map(method -> convertToTuple(item, method))
            .filter(tuple -> Objects.nonNull(tuple._2))
            .map(tuple -> buildApiEntity(tuple, components, apiPath)).filter(Objects::nonNull)
            .peek(callback)
            .collect(Collectors.toList());
    }

    private ApiEntity buildApiEntity(Tuple2<RequestMethod, Operation> tuple,
        Map<String, Schema> components, String apiPath) {
        RequestMethod requestMethod = tuple._1;
        Operation operation = tuple._2;
        ApiEntityBuilder<?, ?> apiEntityBuilder = ApiEntity.builder().apiPath(apiPath)
            .requestMethod(requestMethod)
            .apiName(StringUtils.defaultIfBlank(operation.getSummary(), apiPath))
            .groupId(Objects.requireNonNullElse(operation.getTags(), new ArrayList<String>()).get(0))
            .swaggerId(
                Optional.ofNullable(operation.getOperationId()).orElse(generateSwaggerId(apiPath, requestMethod))
            )
            .apiProtocol(ApiProtocol.HTTPS)
            .apiStatus(ApiStatus.DEVELOP)
            .description(operation.getDescription());

        // Build request Header/PathParam/QueryParam.
        Optional.ofNullable(operation.getParameters()).ifPresent(parameters -> {
            Map<In, List<ParamInfo>> paramMapping = buildParamByInType(
                parameters, components);
            apiEntityBuilder.restfulParams(paramMapping.get(In.QUERY));
            apiEntityBuilder.pathParams(paramMapping.get(In.PATH));
            apiEntityBuilder.requestHeaders(paramMapping.get(In.HEADER));
        });

        // Build request body.
        Optional.ofNullable(operation.getRequestBody())
            .flatMap(requestBody -> requestBody.getContent().entrySet().stream().findFirst()).stream()
            // set api request param type.
            .peek(entry -> apiEntityBuilder
                .apiRequestParamType(Media.resolve(entry.getKey()).getApiRequestParamType()))
            .map(Entry::getValue)
            .peek(mediaType -> ifRequestOrResponseEqualArray(mediaType, apiEntityBuilder::apiRequestJsonType))
            .findFirst()
            .map(MediaType::getSchema)
            .map(schema -> toParams(Optional.empty(), schema, components, 0))
            .ifPresent(apiEntityBuilder::requestParams);

        Optional<ApiResponse> apiResponse = Optional.ofNullable(operation.getResponses())
            .map(response -> response.get(DEFAULT_RESPONSE_KEY));
        // Build response body.
        apiResponse
            .flatMap(response -> Objects.nonNull(response.getContent())
                ? response.getContent().entrySet().stream().findFirst() : Optional.empty()).stream()
            // set api response param type.
            .peek(entry -> apiEntityBuilder
                .apiResponseParamType(Media.resolve(entry.getKey()).getApiRequestParamType()))
            .map(Entry::getValue)
            .peek(mediaType -> ifRequestOrResponseEqualArray(mediaType, apiEntityBuilder::apiResponseJsonType))
            .findFirst()
            .map(MediaType::getSchema)
            .map(schema -> toParams(Optional.empty(), schema, components, 0))
            .ifPresent(apiEntityBuilder::responseParams);
        // Build response header
        apiResponse.map(ApiResponse::getHeaders)
            .ifPresent(headers -> buildResponseHeaders(headers, apiEntityBuilder::responseHeaders));
        return apiEntityBuilder.build();

    }

    private String generateSwaggerId(String apiPath, RequestMethod requestMethod) {
        return Joiner.on(DELIMITER).join(Splitter.on(SPLIT).omitEmptyStrings().split(apiPath)) + DELIMITER
            + requestMethod.name().toLowerCase(Locale.US);
    }

    private void ifRequestOrResponseEqualArray(MediaType mediaType, Consumer<ApiJsonType> callback) {
        Schema<?> schema = mediaType.getSchema();
        if (schema instanceof ArraySchema) {
            callback.accept(ApiJsonType.ARRAY);
        } else {
            callback.accept(ApiJsonType.OBJECT);
        }
    }

    private void buildResponseHeaders(Map<String, Header> headers, Consumer<List<ParamInfo>> callback) {
        List<ParamInfo> paramInfos = headers.entrySet().stream().map(headerEntry -> {
            Header header = headerEntry.getValue();
            Schema<?> schema = header.getSchema();
            SchemaType type = SchemaType.resolve(schema.getType(), schema.getFormat());
            return ParamInfo.builder().required(BooleanUtils.toBoolean(header.getRequired()))
                .description(header.getDescription())
                .key(headerEntry.getKey()).paramType(type.getParamType()).build();
        }).collect(toList());
        callback.accept(paramInfos);
    }

    private Map<In, List<ParamInfo>> buildParamByInType(
        List<Parameter> parameters, Map<String, Schema> components) {
        return parameters.stream().map(parameter -> {
            Schema<?> schema = parameter.getSchema();
            SchemaType type = SchemaType.resolve(schema.getType(), schema.getFormat());
            // The type is null when parameter is Enum.
            if (type == null) {
                String componentKey = getComponentKey(schema);
                schema = components.get(componentKey);
                type = SchemaType.resolve(schema.getType(), schema.getFormat());
            }
            ParamInfo paramInfo =
                ParamInfo.builder().required(BooleanUtils.toBoolean(parameter.getRequired()))
                    .description(parameter.getDescription())
                    .key(parameter.getName()).paramType(type.getParamType()).build();
            In in = In.resolve(parameter.getIn().toUpperCase(Locale.US));
            return Tuple.of(in, paramInfo);
        }).collect(Collectors.groupingBy(Tuple2::_1, mapping(Tuple2::_2, toList())));
    }

    private Tuple2<RequestMethod, Operation> convertToTuple(PathItem item,
        RequestMethod method) {
        String operationMethodName = StringUtils.capitalize(method.name().toLowerCase(Locale.US));
        try {
            Operation operation = (Operation) MethodUtils
                .invokeMethod(item, StringUtils.join(GET, operationMethodName));
            return Tuple.of(method, operation);
        } catch (Exception e) {
            log.warn("Failed to convert Swagger operation. Method={}", method);
            return Tuple.of(method, null);
        }
    }

    private static boolean ifRequired(Schema<?> schema, String paramKey) {
        List<String> required = schema.getRequired();

        return CollectionUtils.isNotEmpty(required) && required.contains(paramKey);
    }


    private static String getComponentKey(Schema schema) {
        String componentKey = (schema instanceof ArraySchema)
            ? ((ArraySchema) schema).getItems().get$ref()
            : schema.get$ref();
        return StringUtils.substringAfterLast(componentKey, COMPONENT_KEY_PATTERN);
    }

    private List<ParamInfo> toParams(Optional<List<List<String>>> pathSummary, Schema<?> schema,
        Map<String, Schema> components, int currentIndex, Schema<?> parentSchema) {
        List<ParamInfo> paramInfos = new ArrayList<>();
        List<List<String>> paths = pathSummary.orElse(new ArrayList<>());
        Map<String, Schema> properties = schema.getProperties();
        paths.add(currentIndex, Lists.newArrayList());

        if (MapUtils.isNotEmpty(properties)) {
            for (Entry<String, Schema> entry : properties.entrySet()) {
                String key = entry.getKey();
                Schema childSchema = entry.getValue();
                SchemaType childSchemaType = SchemaType
                    .resolve(StringUtils.defaultString(childSchema.getType(), SchemaType.OBJECT.getType()),
                        SchemaType.OBJECT.getType());
                ParamInfo childParam = ParamInfo.builder().key(key)
                    .paramType(childSchemaType.getParamType())
                    .description(childSchema.getDescription())
                    .required(ifRequired(schema, key))
                    .build();
                if (List.of(SchemaType.JSON, SchemaType.OBJECT, SchemaType.ARRAY).contains(childSchemaType)) {
                    if (childSchemaType == SchemaType.ARRAY) {
                        List<ParamInfo> arrayChildParam = toComplexParams(paths, childSchema, components, currentIndex);
                        if (CollectionUtils.isNotEmpty(arrayChildParam)) {
                            childParam.setChildParam(List.of(
                                ParamInfo.builder().paramType(ParamType.OBJECT).childParam(arrayChildParam).build()));
                        }
                    } else {
                        childParam.setChildParam(toComplexParams(paths, childSchema, components, currentIndex));
                    }
                }
                paramInfos.add(childParam);
            }

        } else {
            paramInfos = toComplexParams(paths, schema,
                components, currentIndex);
        }
        return ifArrayIncludeObjectElement(parentSchema, paramInfos);
    }

    private List<ParamInfo> toParams(Optional<List<List<String>>> pathSummary, Schema<?> schema,
        Map<String, Schema> components, int currentIndex) {
        return toParams(pathSummary, schema, components, currentIndex, null);
    }

    private List<ParamInfo> ifArrayIncludeObjectElement(Schema<?> parentSchema, List<ParamInfo> paramInfos) {
        if ((Objects.nonNull(parentSchema) && parentSchema instanceof ArraySchema) && !paramInfos.isEmpty()) {
            paramInfos =
                List.of(
                    ParamInfo.builder().childParam(new ArrayList<>(paramInfos)).paramType(ParamType.OBJECT).build());
        }
        return paramInfos;
    }

    private List<ParamInfo> toComplexParams(List<List<String>> paths,
        Schema schema, Map<String, Schema> components, int currentIndex) {
        String componentKey = getComponentKey(schema);
        if (StringUtils.isBlank(componentKey)) {
            return Collections.emptyList();
        }
        if (currentIndex > 0) {
            boolean exist = IntStream.range(0, currentIndex - 1)
                .anyMatch(index -> paths.get(index).contains(componentKey));
            if (exist) {
                return Collections.emptyList();
            }
        }
        paths.get(currentIndex).add(componentKey);
        return toParams(Optional.of(paths), components.get(componentKey), components, currentIndex + 1, schema);
    }

}
