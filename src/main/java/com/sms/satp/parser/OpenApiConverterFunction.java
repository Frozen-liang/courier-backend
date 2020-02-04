package com.sms.satp.parser;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.google.common.collect.Lists;
import com.sms.satp.parser.common.In;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.parser.model.ApiContact;
import com.sms.satp.parser.model.ApiHeader;
import com.sms.satp.parser.model.ApiInfo;
import com.sms.satp.parser.model.ApiOperation;
import com.sms.satp.parser.model.ApiOperation.ApiOperationBuilder;
import com.sms.satp.parser.model.ApiParameter;
import com.sms.satp.parser.model.ApiRequestBody;
import com.sms.satp.parser.model.ApiRequestBody.ApiRequestBodyBuilder;
import com.sms.satp.parser.model.ApiResponse;
import com.sms.satp.parser.model.ApiResponse.ApiResponseBuilder;
import com.sms.satp.parser.model.ApiTag;
import com.sms.satp.parser.schema.ApiSchema;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class OpenApiConverterFunction {

    public static final Function<Contact, ApiContact> CONTACT_CONVERTER = OpenApiConverterFunction::contactConvert;
    public static final Function<Info, ApiInfo> INFO_CONVERTER = OpenApiConverterFunction::infoConvert;
    public static final Function<Tag, ApiTag> TAG_CONVERTER = OpenApiConverterFunction::tagConvert;
    public static final Function<List<Tag>, List<ApiTag>> TAGS_CONVERTER = OpenApiConverterFunction::tagsConvert;
    public static final Function<Parameter, ApiParameter> PARAMETER_CONVERTER =
        OpenApiConverterFunction::parameterConvert;
    public static final Function<List<Parameter>, List<ApiParameter>>
        PARAMETERS_CONVERTER = OpenApiConverterFunction::parametersConvert;
    public static final Function<Schema<?>, ApiSchema> SCHEMA_CONVERTER = OpenApiConverterFunction::schemaConvert;
    public static final Function<Map<String, Schema>, Map<String, ApiSchema>>
        PROPERTIES_CONVERTER = OpenApiConverterFunction::propertiesConvert;
    public static final Function<RequestBody, ApiRequestBody> REQUEST_BODY_CONVERTER =
        OpenApiConverterFunction::requestBodyConvert;
    public static final Function<Map<String, Header>, List<ApiHeader>>
        HEADER_CONVERTER = OpenApiConverterFunction::headersConvert;
    public static final Function<io.swagger.v3.oas.models.responses.ApiResponse, ApiResponse>
        RESPONSE_CONVERTER = OpenApiConverterFunction::responseConvert;
    public static final Function<Operation, ApiOperation.ApiOperationBuilder>
        OPERATION_CONVERTER = OpenApiConverterFunction::operationConvert;

    private static ApiSchema schemaConvert(Schema<?> schema) {
        return Objects.nonNull(schema)
            ? ApiSchema.builder()
            .deprecated(schema.getDeprecated()).description(schema.getDescription()).name(schema.getName())
            .title(schema.getTitle()).required(schema.getRequired()).ref(schema.get$ref())
            .type(SchemaType.resolve(schema.getType())).properties(PROPERTIES_CONVERTER.apply(schema.getProperties()))
            .build() : null;
    }

    private static ApiRequestBody requestBodyConvert(RequestBody requestBody) {
        if (Objects.isNull(requestBody)) {
            return null;
        }
        ApiRequestBodyBuilder builder = ApiRequestBody.builder();
        builder.description(requestBody.getDescription());
        builder.required(requestBody.getRequired());
        Content content = requestBody.getContent();
        builder.mediaTypes(collectMediaTypes(content));
        builder.schema(
            SCHEMA_CONVERTER.apply(requestBody.getContent().values().stream().findFirst().get().getSchema()));
        return builder
            .build();
    }

    private static List<MediaType> collectMediaTypes(Content content) {
        return content
            .keySet().stream().map(MediaType::resolve).collect(toList());
    }

    private static Map<String, ApiSchema> propertiesConvert(Map<String, Schema> properties) {
        return MapUtils.isNotEmpty(properties) ? properties
            .entrySet().stream().collect(toMap(
                Entry::getKey, entry -> SCHEMA_CONVERTER.apply(entry.getValue()))) : null;
    }

    private static List<ApiHeader> headersConvert(Map<String, Header> headers) {
        return MapUtils.isNotEmpty(headers) ? headers.entrySet()
            .stream()
            .map(entry -> ApiHeader.builder().name(entry.getKey()).description(entry.getValue().getDescription())
                .build())
            .collect(toList()) : Lists.newArrayList();
    }

    private static ApiResponse responseConvert(io.swagger.v3.oas.models.responses.ApiResponse response) {
        if (Objects.isNull(response)) {
            return null;
        }
        ApiResponseBuilder builder = ApiResponse.builder();
        builder.description(response.getDescription());
        builder.mediaTypes(collectMediaTypes(response.getContent()));
        builder.headers(HEADER_CONVERTER.apply(response.getHeaders()));
        if (MapUtils.isNotEmpty(response.getContent())) {

            builder.schema(
                SCHEMA_CONVERTER.apply(response.getContent().values().stream().findFirst().get().getSchema()));
        }
        return builder
            .build();
    }

    private static ApiContact contactConvert(Contact contact) {
        return contact != null ? ApiContact
            .builder()
            .email(contact.getEmail())
            .name(contact.getName())
            .url(contact.getUrl())
            .build() : ApiContact.builder().build();
    }

    private static ApiInfo infoConvert(Info info) {
        return Objects.nonNull(info) ? ApiInfo
            .builder()
            .title(info.getTitle())
            .version(info.getVersion())
            .description(info.getDescription())
            .contact(CONTACT_CONVERTER.apply(info.getContact()))
            .build() : null;
    }

    private static ApiTag tagConvert(Tag tag) {
        return ApiTag.builder().name(tag.getName())
            .description(tag.getDescription()).build();
    }

    private static List<ApiTag> tagsConvert(List<Tag> tags) {
        return CollectionUtils.isNotEmpty(tags)
            ? tags
            .stream().map(TAG_CONVERTER).collect(toList()) : Lists.newArrayList();
    }

    private static ApiParameter parameterConvert(Parameter parameter) {
        return ApiParameter
            .builder().deprecated(parameter.getDeprecated()).description(parameter.getDescription())
            .example(Objects.nonNull(parameter.getExample()) ? parameter.getExample().toString() : "")
            .in(In.resolve(StringUtils.upperCase(parameter.getIn())))
            .schemaType(SchemaType.resolve(parameter.getSchema().getType()))
            .name(parameter.getName()).required(parameter.getRequired()).build();
    }

    private static List<ApiParameter> parametersConvert(List<Parameter> parameters) {
        return CollectionUtils.isNotEmpty(parameters)
            ? parameters
            .stream().map(PARAMETER_CONVERTER).collect(toList()) : Lists.newArrayList();
    }

    private static ApiOperationBuilder operationConvert(Operation operation) {
        return ApiOperation
            .builder().deprecated(operation.getDeprecated()).description(operation.getDescription())
            .operationId(operation.getOperationId()).tags(operation.getTags()).summary(operation.getSummary())
            .parameters(PARAMETERS_CONVERTER.apply(operation.getParameters()))
            .apiRequestBody(REQUEST_BODY_CONVERTER.apply(operation.getRequestBody()))
            .apiResponse(RESPONSE_CONVERTER.apply(operation.getResponses().getDefault()));
    }
}
