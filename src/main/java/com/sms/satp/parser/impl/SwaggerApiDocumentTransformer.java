package com.sms.satp.parser.impl;

import static com.sms.satp.common.enums.SchemaType.ARRAY;
import static com.sms.satp.common.enums.SchemaType.JSON;
import static com.sms.satp.common.enums.SchemaType.OBJECT;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.In;
import com.sms.satp.common.enums.Media;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.common.enums.SchemaType;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.api.ApiEntity.ApiEntityBuilder;
import com.sms.satp.entity.api.common.ParamInfo;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.parser.ApiDocumentTransformer;
import com.sms.satp.parser.common.DocumentDefinition;
import com.sms.satp.utils.MD5Util;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

@Slf4j
public enum SwaggerApiDocumentTransformer implements ApiDocumentTransformer<OpenAPI> {

    INSTANCE;

    private static final String COMPONENT_KEY_PATTERN = "#/components/schemas/";
    public static final String GET = "get";
    public static final String DEFAULT_RESPONSE_KEY = "200";

    @Override
    public List<ApiEntity> toApiEntities(DocumentDefinition<OpenAPI> definition) {
        OpenAPI definitionDocument = definition.getDocument();
        Map<String, Schema> schemas = definitionDocument.getComponents().getSchemas();
        final Map<String, List<ParamInfo>> componentReference = prepareComponentReference(schemas);
        return definitionDocument.getPaths().entrySet().stream()
            .map(entry -> buildApiEntities(entry, componentReference))
            .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Set<ApiGroupEntity> toApiGroupEntities(DocumentDefinition<OpenAPI> definition) {
        OpenAPI document = definition.getDocument();
        return document.getPaths().entrySet().stream()
            .map(this::buildGroups)
            .flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Set<ApiGroupEntity> buildGroups(Entry<String, PathItem> entry) {

        PathItem item = entry.getValue();
        return Arrays.stream(RequestMethod.values()).sequential()
            .map(method -> mapTuple(item, method))
            .filter(tuple -> Objects.nonNull(tuple._2))
            .map(Tuple2::_2)
            .map(this::buildApiGroup).filter(Objects::nonNull)
            .collect(Collectors.toSet());

    }

    public ApiGroupEntity buildApiGroup(Operation operation) {
        List<String> tags = operation.getTags();
        return tags.stream().findFirst().map(groupName -> ApiGroupEntity.builder().name(groupName).build())
            .orElse(null);
    }

    private List<ApiEntity> buildApiEntities(Entry<String, PathItem> entry,
        Map<String, List<ParamInfo>> componentReference) {
        PathItem item = entry.getValue();
        String apiPath = entry.getKey();
        return Arrays.stream(RequestMethod.values()).sequential()
            .map(method -> mapTuple(item, method))
            .filter(tuple -> Objects.nonNull(tuple._2))
            .map(tuple -> buildApiEntity(tuple, componentReference, apiPath)).filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private ApiEntity buildApiEntity(Tuple2<RequestMethod, Operation> tuple,
        Map<String, List<ParamInfo>> componentReference, String apiPath) {
        RequestMethod requestMethod = tuple._1;
        Operation operation = tuple._2;

        ApiEntityBuilder<?, ?> apiEntityBuilder = ApiEntity.builder().apiPath(apiPath)
            .requestMethod(requestMethod)
            .apiName(operation.getSummary())
            .swaggerId(operation.getOperationId())
            .apiProtocol(ApiProtocol.HTTPS)
            .apiStatus(ApiStatus.DEVELOP)
            .description(operation.getDescription());

        // Build request Header/PathParam/QueryParam.
        Optional.ofNullable(operation.getParameters()).ifPresent(parameters -> {
            Map<In, List<ParamInfo>> paramMapping = buildParamByInType(
                parameters);
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
            .ifPresent(
                mediaType -> buildRequestOrResponse(componentReference, mediaType,
                    apiEntityBuilder::requestParams));

        Optional<ApiResponse> apiResponse = Optional.ofNullable(operation.getResponses())
            .map(response -> response.get(DEFAULT_RESPONSE_KEY));
        // Build response body.
        apiResponse
            .flatMap(response -> response.getContent().values().stream().findFirst()).stream()
            .peek(mediaType -> ifRequestOrResponseEqualArray(mediaType, apiEntityBuilder::apiResponseJsonType)
            ).findFirst()
            .ifPresent(
                mediaType -> buildRequestOrResponse(componentReference, mediaType,
                    apiEntityBuilder::responseParams));

        // Build response header
        apiResponse.map(ApiResponse::getHeaders)
            .ifPresent(headers -> buildResponseHeaders(headers, apiEntityBuilder::responseHeaders));
        ApiEntity apiEntity = apiEntityBuilder.build();
        apiEntity.setMd5(MD5Util.getMD5(apiEntity));
        return apiEntity;

    }

    private void ifRequestOrResponseEqualArray(MediaType mediaType, Consumer<ApiJsonType> callback) {
        Schema<?> schema = mediaType.getSchema();
        if (schema instanceof ArraySchema) {
            callback.accept(ApiJsonType.ARRAY);
        } else {
            callback.accept(ApiJsonType.OBJECT);
        }
    }

    private void buildRequestOrResponse(Map<String, List<ParamInfo>> componentReference,
        MediaType mediaType, Consumer<List<ParamInfo>> callback) {
        Schema<?> schema = mediaType.getSchema();
        Map<String, Schema> properties = schema.getProperties();
        List<ParamInfo> paramInfos;
        if (MapUtils.isNotEmpty(properties)) {
            paramInfos = properties.entrySet().stream()
                .map(entry -> buildChildParamInfo(null, componentReference, entry))
                .collect(toList());

        } else {
            String componentKey = (schema instanceof ArraySchema)
                ? ((ArraySchema) schema).getItems().get$ref() :
                schema.get$ref();
            paramInfos = componentReference.get(getKey(componentKey));
        }
        callback.accept(paramInfos);


    }

    private void buildResponseHeaders(Map<String, Header> headers, Consumer<List<ParamInfo>> callback) {
        List<ParamInfo> paramInfos = headers.entrySet().stream().map(headerEntry -> {
            Header header = headerEntry.getValue();
            Schema<?> schema = header.getSchema();
            SchemaType type = SchemaType.resolve(schema.getType(), schema.getFormat());
            return ParamInfo.builder().required(header.getRequired()).description(header.getDescription())
                .key(headerEntry.getKey()).paramType(type.getParamType()).build();
        }).collect(toList());
        callback.accept(paramInfos);
    }

    private Map<In, List<ParamInfo>> buildParamByInType(
        List<Parameter> parameters) {
        return parameters.stream().map(parameter -> {
            Schema<?> schema = parameter.getSchema();
            SchemaType type = SchemaType.resolve(schema.getType(), schema.getFormat());
            ParamInfo paramInfo =
                ParamInfo.builder().required(parameter.getRequired()).description(parameter.getDescription())
                    .key(parameter.getName()).paramType(type.getParamType()).build();
            In in = In.resolve(parameter.getIn().toUpperCase(Locale.US));
            return Tuple.of(in, paramInfo);
        }).collect(Collectors.groupingBy(Tuple2::_1, mapping(Tuple2::_2, toList())));
    }

    private Tuple2<RequestMethod, Operation> mapTuple(PathItem item,
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

    private Map<String, List<ParamInfo>> prepareComponentReference(Map<String, Schema> schemas) {
        Map<String, List<ParamInfo>> prepareReference = schemas.keySet().stream()
            .collect(Collectors.toConcurrentMap(Function.identity(), key -> new ArrayList<>()));
        return prepareReference.entrySet().parallelStream()
            .collect(
                Collectors.toMap(Entry::getKey, entry -> buildJsonParamInfo(entry, schemas, prepareReference)));
    }

    private List<ParamInfo> buildJsonParamInfo(Entry<String, List<ParamInfo>> entry, Map<String, Schema> schemas,
        Map<String, List<ParamInfo>> paramInfoReference) {
        Schema<?> schema = schemas.get(entry.getKey());
        Optional<Map<String, Schema>> properties = Optional.ofNullable(schema.getProperties());

        List<ParamInfo> childParams = properties.orElse(new HashMap<>()).entrySet().stream()
            .map(propertiesEntry -> buildChildParamInfo(entry.getKey(), paramInfoReference, propertiesEntry))
            .peek(paramInfo -> ifRequired(schema, paramInfo))
            .collect(Collectors.toList());
        List<ParamInfo> value = entry.getValue();
        value.addAll(childParams);
        return value;
    }

    private void ifRequired(Schema<?> schema, ParamInfo paramInfo) {
        List<String> required = schema.getRequired();
        if (CollectionUtils.isNotEmpty(required) && required.contains(paramInfo.getKey())) {
            paramInfo.setRequired(true);
        }
    }

    private ParamInfo buildChildParamInfo(String parentComponentKey,
        Map<String, List<ParamInfo>> paramInfoReference, Entry<String, Schema> propertiesEntry) {
        Schema<?> childSchema = propertiesEntry.getValue();
        String type = StringUtils.defaultString(childSchema.getType(), OBJECT.getType());
        SchemaType childSchemaType = SchemaType.resolve(type, childSchema.getFormat());
        ParamInfo childParam = ParamInfo.builder().key(propertiesEntry.getKey())
            .paramType(childSchemaType.getParamType()).description(childSchema.getDescription()).build();
        if (List.of(JSON, OBJECT, ARRAY).contains(childSchemaType)) {
            String componentKey = (childSchema instanceof ArraySchema)
                ? ((ArraySchema) childSchema).getItems().get$ref()
                : childSchema.get$ref();
            Tuple2<Boolean, List<ParamInfo>> tuple = buildChildReference(parentComponentKey, componentKey,
                paramInfoReference);
            childParam.setReference(tuple._1);
            childParam.setChildParam(tuple._2);
        }

        return childParam;
    }

    private Tuple2<Boolean, List<ParamInfo>> buildChildReference(String parentComponentKey, String ref,
        Map<String, List<ParamInfo>> paramInfoMap) {
        String componentKey = getKey(ref);
        Supplier<List<ParamInfo>> fetchReference = () -> StringUtils.isNotBlank(componentKey)
            ? paramInfoMap.get(componentKey) : Collections.emptyList();
        return parentComponentKey.equals(componentKey) ? Tuple.of(true, Collections.emptyList()) : Tuple.of(false,
            fetchReference.get());

    }

    private static String getKey(String ref) {
        return StringUtils.substringAfterLast(ref, COMPONENT_KEY_PATTERN);
    }

}
