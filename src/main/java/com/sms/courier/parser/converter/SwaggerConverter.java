package com.sms.courier.parser.converter;

import com.sms.courier.common.enums.SchemaType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.models.ArrayModel;
import io.swagger.models.ComposedModel;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.RefPath;
import io.swagger.models.RefResponse;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.RefParameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ComposedProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.SwaggerResolver;
import io.swagger.parser.util.SwaggerDeserializationResult;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.FileSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.converter.SwaggerInventory;
import io.swagger.v3.parser.core.extensions.SwaggerParserExtension;
import io.swagger.v3.parser.core.models.AuthorizationValue;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.parser.util.SchemaTypeUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@SuppressFBWarnings
@Slf4j
public class SwaggerConverter implements SwaggerParserExtension {

    private static final List<SchemaType> OBJECT_TYPE = List.of(SchemaType.ARRAY, SchemaType.JSON, SchemaType.OBJECT);

    @Override
    public SwaggerParseResult readLocation(String url, List<AuthorizationValue> auths, ParseOptions options) {
        boolean resolve = false;
        if (options != null) {
            resolve = options.isResolve();
        }

        SwaggerDeserializationResult result = new SwaggerParser().readWithInfo(url, convert(auths), resolve);

        return readResult(result, auths, options);
    }

    @Override
    public SwaggerParseResult readContents(String swaggerAsString, List<AuthorizationValue> auth,
        ParseOptions options) {
        SwaggerDeserializationResult result = new SwaggerParser().readWithInfo(swaggerAsString,
            options == null || options.isResolve());

        if (options != null) {
            if (options.isResolve()) {
                Swagger resolved = new SwaggerResolver(result.getSwagger(), convert(auth)).resolve();
                result.setSwagger(resolved);
            }
        }
        return readResult(result, auth, options);

    }

    private SwaggerParseResult readResult(SwaggerDeserializationResult result, List<AuthorizationValue> auth,
        ParseOptions options) {
        SwaggerParseResult out = convert(result);
        if (out != null && options != null && options.isFlatten()) {
            try {
                SwaggerParseResult resultV3 = new OpenAPIV3Parser()
                    .readContents(Yaml.pretty(out.getOpenAPI()), auth, options);
                out.setOpenAPI(resultV3.getOpenAPI());
                if (out.getMessages() != null) {
                    out.getMessages().addAll(resultV3.getMessages());
                    out.messages(out.getMessages().stream()
                        .distinct()
                        .collect(Collectors.toList()));
                } else {
                    out.messages(resultV3.getMessages());
                }
            } catch (Exception e) {
                log.warn("Failed to read API message {}", e.getMessage());
            }
        }
        return out;
    }

    public List<io.swagger.models.auth.AuthorizationValue> convert(List<AuthorizationValue> auths) {
        List<io.swagger.models.auth.AuthorizationValue> convertedAuth = new ArrayList<>();
        if (auths != null) {
            for (AuthorizationValue auth : auths) {
                io.swagger.models.auth.AuthorizationValue v = new io.swagger.models.auth.AuthorizationValue();
                v.setType(auth.getType());
                v.setValue(auth.getValue());
                v.setKeyName(auth.getKeyName());
                convertedAuth.add(v);
            }
        }

        return convertedAuth;
    }

    public Operation convert(io.swagger.models.Operation v2Operation, List<String> globalConsumes,
        List<String> globalProduces, Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters,
        Components components) {
        Operation operation = new Operation();
        if (StringUtils.isNotBlank(v2Operation.getDescription())) {
            operation.setDescription(v2Operation.getDescription());
        }
        if (StringUtils.isNotBlank(v2Operation.getSummary())) {
            operation.setSummary(v2Operation.getSummary());
        }
        operation.setDeprecated(v2Operation.isDeprecated());
        operation.setOperationId(v2Operation.getOperationId());
        operation.setExtensions(convert(v2Operation.getVendorExtensions()));

        operation.setTags(v2Operation.getTags());

        if (v2Operation.getParameters() != null) {
            List<io.swagger.models.parameters.Parameter> formParams = new ArrayList<>();
            List<io.swagger.models.parameters.Parameter> bodyParams = new ArrayList<>();
            for (io.swagger.models.parameters.Parameter param : v2Operation.getParameters()) {

                if ("formData".equals(param.getIn())) {
                    formParams.add(param);
                } else if ("body".equals(param.getIn())) {
                    bodyParams.add(param);
                    operation.addExtension("x-codegen-request-body-name", param.getName());
                    /*operation.setRequestBody(
                        convertParameterToRequestBody(param, v2Operation.getConsumes(), globalConsumes));
                    operation.addExtension("x-codegen-request-body-name", param.getName());*/
                } else {
                    Parameter convert = convert(param, components);
                    String ref = convert.get$ref();
                    if (ref != null && ref.startsWith("#/components/requestBodies/") && isRefABodyParam(param,
                        globalV2Parameters)) {
                        operation.setRequestBody(new RequestBody().$ref(ref));
                    } else if (ref != null && ref.startsWith("#/components/schemas/") && isRefAFormParam(param,
                        globalV2Parameters)) {
                        formParams.add(param);
                    } else {
                        operation.addParametersItem(convert);
                    }
                }
            }

            if (bodyParams.size() > 0) {
                RequestBody requestBody = convertParametersToRequestBody(bodyParams, v2Operation.getConsumes(),
                    globalConsumes);
                requestBody.getContent().forEach((key, content) -> {
                    Schema schema = content.getSchema();
                    if (schema != null && schema.getRequired() != null && schema.getRequired().size() > 0) {
                        requestBody.setRequired(Boolean.TRUE);
                    }
                });
                operation.setRequestBody(requestBody);
            }

            if (formParams.size() > 0) {
                RequestBody body = convertFormDataToRequestBody(formParams, v2Operation.getConsumes(), globalConsumes,
                    globalV2Parameters, components);
                body.getContent().forEach((key, content) -> {
                    Schema schema = content.getSchema();
                    if (schema != null && schema.getRequired() != null && schema.getRequired().size() > 0) {
                        body.setRequired(Boolean.TRUE);
                    }
                });
                operation.requestBody(body);
            }

        }

        if (v2Operation.getResponses() != null) {
            for (String responseCode : v2Operation.getResponses().keySet()) {
                io.swagger.models.Response v2Response = v2Operation.getResponses().get(responseCode);
                ApiResponse response = convert(v2Response, v2Operation.getProduces(), globalProduces);
                ApiResponses responses = operation.getResponses();
                if (responses == null) {
                    responses = new ApiResponses();
                }

                operation.responses(responses.addApiResponse(responseCode, response));
            }
        }

        if (v2Operation.getExternalDocs() != null) {
            operation.setExternalDocs(convert(v2Operation.getExternalDocs()));
        }

        if (v2Operation.getSecurity() != null && v2Operation.getSecurity().size() > 0) {
            operation.setSecurity(convertSecurityRequirementsMap(v2Operation.getSecurity()));
        }

        return operation;
    }

    private Map<String, Object> convert(Map<String, Object> vendorExtensions) {
        if (vendorExtensions != null && vendorExtensions.size() > 0) {
            vendorExtensions.entrySet().removeIf(extension -> (
                extension.getKey().equals("x-example"))
                || extension.getKey().equals("x-examples")
                || extension.getKey().equals("x-nullable"));
        }

        return vendorExtensions;
    }

    public Info convert(io.swagger.models.Info v2Info) {
        Info info = new Info();

        info.setContact(convert(v2Info.getContact()));
        info.setDescription(v2Info.getDescription());
        info.setLicense(convert(v2Info.getLicense()));
        info.setTermsOfService(v2Info.getTermsOfService());
        info.setTitle(v2Info.getTitle());
        info.setVersion(v2Info.getVersion());
        info.setExtensions(convert(v2Info.getVendorExtensions()));

        return info;
    }

    private License convert(io.swagger.models.License v2License) {
        if (v2License == null) {
            return null;
        }

        License license = new License();
        license.setExtensions(convert(v2License.getVendorExtensions()));
        license.setName(v2License.getName());
        license.setUrl(v2License.getUrl());

        return license;
    }

    public Contact convert(io.swagger.models.Contact v2Contact) {
        if (v2Contact == null) {
            return null;
        }

        Contact contact = new Contact();

        contact.setUrl(v2Contact.getUrl());
        contact.setName(v2Contact.getName());
        contact.setEmail(v2Contact.getEmail());

        // TODO - treat this process after adding extensions to v2Contact object

        return contact;
    }

    public PathItem convert(Path v2Path, List<String> globalConsumes, List<String> globalProduces,
        Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters, Components components) {
        PathItem v3Path = new PathItem();

        if (v2Path instanceof RefPath) {

            v3Path.set$ref(((RefPath) v2Path).get$ref());
        } else {

            if (v2Path.getParameters() != null) {
                for (io.swagger.models.parameters.Parameter param : v2Path.getParameters()) {
                    v3Path.addParametersItem(convert(param, components));
                }
            }

            io.swagger.models.Operation v2Operation;

            v2Operation = v2Path.getGet();
            if (v2Operation != null) {
                v3Path.setGet(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getPut();
            if (v2Operation != null) {
                v3Path.setPut(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getPost();
            if (v2Operation != null) {
                v3Path.setPost(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getPatch();
            if (v2Operation != null) {
                v3Path.setPatch(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getDelete();
            if (v2Operation != null) {
                v3Path.setDelete(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getHead();
            if (v2Operation != null) {
                v3Path.setHead(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }
            v2Operation = v2Path.getOptions();
            if (v2Operation != null) {
                v3Path.setOptions(convert(v2Operation, globalConsumes, globalProduces, globalV2Parameters, components));
            }

            v3Path.setExtensions(convert(v2Path.getVendorExtensions()));
        }

        return v3Path;
    }

    private ExternalDocumentation convert(ExternalDocs externalDocs) {
        ExternalDocumentation externalDocumentation = new ExternalDocumentation();

        externalDocumentation.setUrl(externalDocs.getUrl());
        externalDocumentation.setDescription(externalDocs.getDescription());
        Map<String, Object> extensions = convert(externalDocs.getVendorExtensions());
        if (extensions != null && extensions.size() > 0) {
            externalDocumentation.setExtensions(extensions);
        }

        return externalDocumentation;
    }

    private List<Server> convert(List<Scheme> schemes, String host, String basePath) {
        List<Server> servers = new ArrayList<>();
        String baseUrl;

        if (StringUtils.isNotEmpty(basePath)) {
            baseUrl = basePath;
        } else {
            baseUrl = "/";
        }

        if (StringUtils.isNotEmpty(host)) {
            baseUrl = host + baseUrl;
        }

        if (!StringUtils.startsWith(baseUrl, "/") && schemes != null && !schemes.isEmpty()) {
            for (Scheme scheme : schemes) {
                Server server = new Server();
                server.setUrl(scheme.toValue() + "://" + baseUrl);

                servers.add(server);
            }
        } else {
            if (!StringUtils.startsWith(baseUrl, "/") && !"/".equals(baseUrl)) {
                baseUrl = "//" + baseUrl;
            }
            Server server = new Server();
            server.setUrl(baseUrl);
            servers.add(server);
        }

        return servers;
    }

    private SecurityScheme convert(SecuritySchemeDefinition definition) {
        SecurityScheme securityScheme;

        switch (definition.getType()) {
            case "basic":
                securityScheme = createBasicSecurityScheme();
                break;
            case "apiKey":
                securityScheme = convertApiKeySecurityScheme(definition);
                break;
            case "oauth2":
                securityScheme = convertOauth2SecurityScheme(definition);
                break;
            default:
                securityScheme = new SecurityScheme();
        }

        securityScheme.setDescription(definition.getDescription());
        securityScheme.setExtensions(convert(definition.getVendorExtensions()));

        return securityScheme;
    }

    private Schema convert(SerializableParameter sp) {
        Schema schema;
        if ("file".equals(sp.getType())) {
            schema = new FileSchema();
        } else if ("array".equals(sp.getType())) {
            ArraySchema as = new ArraySchema();
            if (sp.getItems() != null) {
                as.setItems(convert(sp.getItems()));
            }
            schema = as;
        } else {
            PrimitiveType ptype = PrimitiveType.fromTypeAndFormat(sp.getType(), sp.getFormat());
            if (ptype != null) {
                schema = ptype.createProperty();
            } else {
                ptype = PrimitiveType.fromTypeAndFormat(sp.getType(), null);
                if (ptype != null) {
                    schema = ptype.createProperty();
                } else {
                    schema = new Schema();
                    schema.setType(sp.getType());
                }
                schema.setFormat(sp.getFormat());
            }
        }

        schema.setDescription(sp.getDescription());
        schema.setReadOnly(sp.isReadOnly());
        schema.setEnum(sp.getEnum());
        ifNotNull(sp.getMaxItems(), schema::setMaxLength);
        ifNotNull(sp.getMinItems(), schema::setMinItems);
        ifNotNull(sp.isUniqueItems(), schema::setUniqueItems);
        schema.setMaximum(sp.getMaximum());
        schema.setExclusiveMaximum(sp.isExclusiveMaximum());
        schema.setMinimum(sp.getMinimum());
        schema.setExclusiveMinimum(sp.isExclusiveMinimum());
        schema.setMinLength(sp.getMinLength());
        schema.setMaxLength(sp.getMaxLength());
        schema.setName(sp.getName());

        if (sp.getVendorExtensions() != null) {
            Object exampleExtension = sp.getVendorExtensions().get("x-example");
            if (exampleExtension != null) {
                schema.setExample(exampleExtension);
            }
            Object nullableExtension = sp.getVendorExtensions().get("x-nullable");
            if (nullableExtension != null) {
                schema.setNullable((Boolean) nullableExtension);
            }
            schema.setExtensions(convert(sp.getVendorExtensions()));
        }

        if (sp.getMultipleOf() != null) {
            schema.setMultipleOf(new BigDecimal(sp.getMultipleOf().toString()));
        }

        schema.setPattern(sp.getPattern());

        if (sp instanceof AbstractSerializableParameter) {
            AbstractSerializableParameter ap = (AbstractSerializableParameter) sp;
            schema.setDefault(ap.getDefault());
        }
        return schema;
    }

    public ApiResponse convert(Response response, List<String> globalProduces) {
        return convert(response, null, globalProduces);
    }

    private Schema convert(Property schema) {
        if (schema == null) {
            return null;
        }
        Schema result;

        if (schema instanceof RefProperty) {
            RefProperty ref = (RefProperty) schema;
            if (ref.get$ref().indexOf("#/definitions") == 0) {
                String updatedRef = "#/components/schemas" + ref.get$ref().substring("#/definitions".length());
                ref.set$ref(updatedRef);
            }

            result = new Schema();
            result.set$ref(ref.get$ref());
        } else if (schema instanceof ArrayProperty) {
            ArraySchema arraySchema = Json.mapper().convertValue(schema, ArraySchema.class);

            Property items = ((ArrayProperty) schema).getItems();
            Schema itemsSchema = convert(items);
            arraySchema.setItems(itemsSchema);

            if (((ArrayProperty) schema).getMaxItems() != null) {
                arraySchema.setMaxItems(((ArrayProperty) schema).getMaxItems());
            }
            if (((ArrayProperty) schema).getMinItems() != null) {
                arraySchema.setMinItems(((ArrayProperty) schema).getMinItems());
            }
            if (((ArrayProperty) schema).getUniqueItems() != null && ((ArrayProperty) schema).getUniqueItems()) {
                arraySchema.setUniqueItems(((ArrayProperty) schema).getUniqueItems());
            }

            result = arraySchema;

        } else if (schema instanceof FileProperty) {
            result = Json.mapper().convertValue(schema, FileSchema.class);

        } else {

            result = Json.mapper().convertValue(schema, Schema.class);
            result.setExample(schema.getExample());

            if ("object".equals(schema.getType()) && (result.getProperties() != null) && (result.getProperties().size()
                > 0)) {
                Map<String, Schema> properties = new LinkedHashMap<>();

                ((ObjectProperty) schema).getProperties().forEach((k, v) -> properties.put(k, convert(v)));

                result.setProperties(properties);
            }

            if (schema instanceof MapProperty) {
                MapProperty map = (MapProperty) schema;

                result.setAdditionalProperties(convert(map.getAdditionalProperties()));
                result.setMinProperties(map.getMinProperties());
                result.setMaxProperties(map.getMaxProperties());
            }

            if (schema instanceof AbstractNumericProperty) {
                AbstractNumericProperty np = (AbstractNumericProperty) schema;

                result.setExclusiveMaximum(np.getExclusiveMaximum());
                result.setExclusiveMinimum(np.getExclusiveMinimum());
            }

            if (schema instanceof StringProperty) {
                StringProperty sp = (StringProperty) schema;

                result.setMinLength(sp.getMinLength());
                result.setMaxLength(sp.getMaxLength());
                result.setPattern(sp.getPattern());
            }
        }

        if (schema.getVendorExtensions() != null) {
            Object nullableExtension = schema.getVendorExtensions().get("x-nullable");
            if (nullableExtension != null) {
                result.setNullable((Boolean) nullableExtension);
            }

            result.setExtensions(convert(schema.getVendorExtensions()));
        }

        return result;
    }

    @SuppressFBWarnings
    public ApiResponse convert(io.swagger.models.Response v2Response, List<String> produces,
        List<String> globalProduces) {
        ApiResponse response = new ApiResponse();
        Content content = new Content();

        if (v2Response instanceof RefResponse) {

            RefResponse ref = (RefResponse) v2Response;
            if (ref.get$ref().indexOf("#/responses") == 0) {
                String updatedRef = "#/components/responses" + ref.get$ref().substring("#/responses".length());
                ref.set$ref(updatedRef);
            }

            response.set$ref(ref.get$ref());
        } else {

            List<String> mediaTypes = new ArrayList<>(globalProduces);
            if (produces != null) {
                // use this for media type
                mediaTypes.clear();
                mediaTypes.addAll(produces);
            }

            if (mediaTypes.size() == 0) {
                mediaTypes.add("*/*");
            }

            response.setDescription(v2Response.getDescription());

            if (v2Response.getSchema() != null) {
                Schema schema = convertFileSchema(convert(v2Response.getSchema()));
                for (String type : mediaTypes) {
                    // TODO: examples
                    MediaType mediaType = new MediaType();
                    content.addMediaType(type, mediaType.schema(schema));
                }
                response.content(content);
            }

            response.content(convertExamples(v2Response.getExamples(), content));
            response.setExtensions(convert(v2Response.getVendorExtensions()));

            if (v2Response.getHeaders() != null && v2Response.getHeaders().size() > 0) {
                response.setHeaders(convertHeaders(v2Response.getHeaders()));
            }
        }

        return response;
    }

    public Parameter convert(io.swagger.models.parameters.Parameter v2Parameter, Components components) {
        Parameter v3Parameter = new Parameter();

        if (StringUtils.isNotBlank(v2Parameter.getDescription())) {
            v3Parameter.setDescription(v2Parameter.getDescription());
        }
        if (v2Parameter instanceof SerializableParameter) {
            v3Parameter.setAllowEmptyValue(((SerializableParameter) v2Parameter).getAllowEmptyValue());
        }
        v3Parameter.setIn(v2Parameter.getIn());
        v3Parameter.setName(v2Parameter.getName());

        Object exampleExtension = v2Parameter.getVendorExtensions().get("x-example");
        if (exampleExtension != null) {
            v3Parameter.setExample(exampleExtension);
        }

        Schema schema = null;

        if (v2Parameter instanceof RefParameter) {

            RefParameter ref = (RefParameter) v2Parameter;
            if (ref.get$ref().indexOf("#/parameters") == 0) {
                String updatedRef = "#/components/";
                if (components.getRequestBodies() != null
                    && components.getRequestBodies().get(ref.getSimpleRef()) != null) {
                    updatedRef += "requestBodies";
                } else if (components.getSchemas() != null
                    && components.getSchemas().get("formData_" + ref.getSimpleRef()) != null) {
                    updatedRef += "schemas";
                } else {
                    updatedRef += "parameters";
                }
                updatedRef += ref.get$ref().substring("#/parameters".length());
                ref.set$ref(updatedRef);
            }

            v3Parameter.set$ref(ref.get$ref());
        } else if (v2Parameter instanceof SerializableParameter) {
            SerializableParameter sp = (SerializableParameter) v2Parameter;

            if ("array".equals(sp.getType())) {
                ArraySchema a = new ArraySchema();
                // TODO: convert arrays to proper template format

                String cf = sp.getCollectionFormat();

                if (StringUtils.isEmpty(cf)) {
                    cf = "csv";
                }

                switch (cf) {
                    case "ssv":
                        if ("query".equals(v2Parameter.getIn())) {
                            v3Parameter.setStyle(Parameter.StyleEnum.SPACEDELIMITED);
                        }
                        break;
                    case "pipes":
                        if ("query".equals(v2Parameter.getIn())) {
                            v3Parameter.setStyle((Parameter.StyleEnum.PIPEDELIMITED));
                        }
                        break;
                    case "tsv":
                        break;
                    case "multi":
                        if ("query".equals(v2Parameter.getIn())) {
                            v3Parameter.setStyle(Parameter.StyleEnum.FORM);
                            v3Parameter.setExplode(true);
                        }
                        break;
                    case "csv":
                    default:
                        if ("query".equals(v2Parameter.getIn())) {
                            v3Parameter.setStyle(Parameter.StyleEnum.FORM);
                            v3Parameter.setExplode(false);
                        } else if ("header".equals(v2Parameter.getIn()) || "path".equals(v2Parameter.getIn())) {
                            v3Parameter.setStyle(Parameter.StyleEnum.SIMPLE);
                            v3Parameter.setExplode(false);
                        }
                }

                Property items = sp.getItems();
                Schema itemsSchema = convert(items);
                a.setItems(itemsSchema);

                ifNotNull(sp.getMaxItems(), a::setMaxLength);
                ifNotNull(sp.getMinItems(), a::setMinItems);
                ifNotNull(sp.isUniqueItems(), a::setUniqueItems);

                schema = a;
            } else {
                schema = SchemaTypeUtil.createSchema(sp.getType(), sp.getFormat());
                schema.setType(sp.getType());
                schema.setFormat(sp.getFormat());

                schema.setMaximum(sp.getMaximum());
                schema.setExclusiveMaximum(sp.isExclusiveMaximum());
                schema.setMinimum(sp.getMinimum());
                schema.setExclusiveMinimum(sp.isExclusiveMinimum());
                schema.setMinLength(sp.getMinLength());
                schema.setMaxLength(sp.getMaxLength());
                if (sp.getMultipleOf() != null) {
                    schema.setMultipleOf(new BigDecimal(sp.getMultipleOf().toString()));
                }
                schema.setPattern(sp.getPattern());
            }

            if (sp.getEnum() != null) {
                for (String e : sp.getEnum()) {
                    switch (sp.getType() == null ? SchemaTypeUtil.OBJECT_TYPE : sp.getType()) {
                        case SchemaTypeUtil.INTEGER_TYPE:
                            schema.addEnumItemObject(Integer.parseInt(e));
                            break;
                        case SchemaTypeUtil.NUMBER_TYPE:
                            schema.addEnumItemObject(new BigDecimal(e));
                            break;
                        case SchemaTypeUtil.BOOLEAN_TYPE:
                            schema.addEnumItemObject(Boolean.valueOf(e));
                            break;
                        default:
                            schema.addEnumItemObject(e);
                            break;
                    }
                }
            }

            if (sp.getVendorExtensions() != null) {
                Object nullableExtension = sp.getVendorExtensions().get("x-nullable");
                if (nullableExtension != null) {
                    schema.setNullable((Boolean) nullableExtension);
                }
            }

            schema.setExtensions(convert(sp.getVendorExtensions()));

            if (sp instanceof AbstractSerializableParameter) {
                AbstractSerializableParameter ap = (AbstractSerializableParameter) sp;
                schema.setDefault(ap.getDefault());
            }
        }

        if (v2Parameter.getRequired()) {
            v3Parameter.setRequired(v2Parameter.getRequired());
        }
        v3Parameter.setSchema(schema);
        v3Parameter.setExtensions(convert(v2Parameter.getVendorExtensions()));
        return v3Parameter;
    }

    public Schema convert(io.swagger.models.Model v2Model) {
        if (v2Model == null) {
            return null;
        }
        Schema result;

        if (v2Model instanceof ArrayModel) {
            ArraySchema arraySchema = Json.mapper().convertValue(v2Model, ArraySchema.class);

            arraySchema.setItems(convert(((ArrayModel) v2Model).getItems()));

            result = arraySchema;
        } else if (v2Model instanceof ComposedModel) {
            ComposedModel composedModel = (ComposedModel) v2Model;
            ComposedSchema composed = new ComposedSchema();
            composed.setDescription(composedModel.getDescription());
            composed.setExample(composedModel.getExample());
            if (composedModel.getExternalDocs() != null) {
                composed.setExternalDocs(convert(composedModel.getExternalDocs()));
            }
            composed.setTitle(composedModel.getTitle());
            composed.setExtensions(convert(composedModel.getVendorExtensions()));
            composed.setAllOf(composedModel.getAllOf().stream().map(this::convert).collect(Collectors.toList()));

            addProperties(v2Model, composed);

            result = composed;
        } else {
            String v2discriminator = null;

            if (v2Model instanceof ModelImpl) {
                ModelImpl model = (ModelImpl) v2Model;

                v2discriminator = model.getDiscriminator();
                model.setDiscriminator(null);
            }

            result = Json.mapper().convertValue(v2Model, Schema.class);

            addProperties(v2Model, result);

            if (v2Model instanceof ModelImpl) {
                ModelImpl model = (ModelImpl) v2Model;

                if (model.getAdditionalProperties() != null) {
                    result.setAdditionalProperties(convert(model.getAdditionalProperties()));
                }
            } else if (v2Model instanceof RefModel) {
                RefModel ref = (RefModel) v2Model;
                if (ref.get$ref().indexOf("#/definitions") == 0) {
                    String updatedRef = "#/components/schemas" + ref.get$ref().substring("#/definitions".length());
                    result.set$ref(updatedRef);
                }
            }

            if (v2discriminator != null) {
                Discriminator discriminator = new Discriminator();

                discriminator.setPropertyName(v2discriminator);
                result.setDiscriminator(discriminator);
            }
        }

        if (v2Model.getVendorExtensions() != null) {
            Object nullableExtension = v2Model.getVendorExtensions().get("x-nullable");
            if (nullableExtension != null) {
                result.setNullable((Boolean) nullableExtension);
            }
        }

        return result;
    }

    public SwaggerParseResult convert(SwaggerDeserializationResult parse) {
        if (parse == null) {
            return null;
        }

        SwaggerParseResult output = new SwaggerParseResult().messages(parse.getMessages());

        if (parse.getSwagger() == null) {
            return output;
        }

        OpenAPI openapi = new OpenAPI();
        final SwaggerInventory inventory = new SwaggerInventory().process(parse.getSwagger());
        final List<String> globalConsumes = new ArrayList<>();
        final List<String> globalProduces = new ArrayList<>();
        Swagger swagger = parse.getSwagger();

        if (swagger.getVendorExtensions() != null) {
            openapi.setExtensions(convert(swagger.getVendorExtensions()));
        }
        // Set extension to retain original version of OAS document.
        openapi.addExtension("x-original-swagger-version", swagger.getSwagger());

        if (swagger.getExternalDocs() != null) {
            openapi.setExternalDocs(convert(swagger.getExternalDocs()));
        }

        if (swagger.getInfo() != null) {
            openapi.setInfo(convert(swagger.getInfo()));
        }

        openapi.setServers(convert(swagger.getSchemes(), swagger.getHost(), swagger.getBasePath()));

        if (swagger.getTags() != null) {
            openapi.setTags(convertTags(swagger.getTags()));
        }

        if (swagger.getConsumes() != null) {
            globalConsumes.addAll(swagger.getConsumes());
        }

        if (swagger.getProduces() != null) {
            globalProduces.addAll(swagger.getProduces());
        }

        if (swagger.getSecurity() != null && swagger.getSecurity().size() > 0) {
            openapi.setSecurity(convertSecurityRequirements(swagger.getSecurity()));
        }

        List<Model> models = inventory.getModels();

        // TODO until we have the example object working correctly in v3 pojos...
        for (Model model : models) {
            if (model instanceof RefModel) {
                RefModel ref = (RefModel) model;
                if (ref.get$ref().indexOf("#/definitions") == 0) {
                    String updatedRef = "#/components/schemas" + ref.get$ref().substring("#/definitions".length());
                    ref.set$ref(updatedRef);
                }
            }
        }

        for (Property property : inventory.getProperties()) {
            if (property instanceof RefProperty) {
                RefProperty ref = (RefProperty) property;
                if (ref.get$ref().indexOf("#/definitions") == 0) {
                    String updatedRef = "#/components/schemas" + ref.get$ref().substring("#/definitions".length());
                    ref.set$ref(updatedRef);
                }
            }

            if (property instanceof ComposedProperty) {
                ComposedProperty comprop = (ComposedProperty) property;
                if (comprop.getAllOf() != null) {
                    for (Property item : comprop.getAllOf()) {
                        if (item instanceof RefProperty) {
                            RefProperty ref = (RefProperty) item;
                            if (ref.get$ref().indexOf("#/definitions") == 0) {
                                String updatedRef =
                                    "#/components/schemas" + ref.get$ref().substring("#/definitions".length());
                                ref.set$ref(updatedRef);
                            }


                        }

                    }
                }
            }
        }

        final Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters = new HashMap<>();
        final Components components = new Components();
        if (swagger.getParameters() != null) {
            globalV2Parameters.putAll(swagger.getParameters());
            swagger.getParameters().forEach((k, v) -> {
                if ("body".equals(v.getIn())) {
                    components.addRequestBodies(k, convertParameterToRequestBody(v, globalConsumes));
                } else if ("formData".equals(v.getIn())) {
                    // formData_ is added not to overwrite existing schemas
                    components.addSchemas("formData_" + k, convertFormDataToSchema(v));
                } else {
                    components.addParameters(k, convert(v, components));
                }
            });
        }

        Paths v3Paths = new Paths();
        Map<String, Path> pathMap = Optional.ofNullable(swagger.getPaths()).orElse(new HashMap<>());
        for (String pathname : pathMap.keySet()) {
            io.swagger.models.Path v2Path = swagger.getPath(pathname);
            PathItem v3Path = convert(v2Path, globalConsumes, globalProduces, globalV2Parameters, components);
            v3Paths.put(pathname, v3Path);
        }
        openapi.setPaths(v3Paths);

        if (swagger.getResponses() != null) {
            swagger.getResponses().forEach((k, v) -> components.addResponses(k, convert(v, globalProduces)));
        }

        if (swagger.getDefinitions() != null) {
            for (String key : swagger.getDefinitions().keySet()) {
                Model model = swagger.getDefinitions().get(key);
                Schema schema = convert(model);

                components.addSchemas(key, schema);
            }
        }

        if (swagger.getSecurityDefinitions() != null) {
            swagger.getSecurityDefinitions().forEach((k, v) -> components.addSecuritySchemes(k, convert(v)));
        }

        openapi.setComponents(components);

        output.setOpenAPI(openapi);

        return output;
    }

    private List<io.swagger.v3.oas.models.security.SecurityRequirement> convertSecurityRequirements(
        List<SecurityRequirement> security) {
        List<io.swagger.v3.oas.models.security.SecurityRequirement> securityRequirements = new ArrayList<>();

        for (SecurityRequirement requirement : security) {
            io.swagger.v3.oas.models.security.SecurityRequirement securityRequirement =
                new io.swagger.v3.oas.models.security.SecurityRequirement();

            requirement.getRequirements().forEach(securityRequirement::addList);

            securityRequirements.add(securityRequirement);
        }

        return securityRequirements;
    }

    private List<io.swagger.v3.oas.models.security.SecurityRequirement> convertSecurityRequirementsMap(
        List<Map<String, List<String>>> security) {
        List<io.swagger.v3.oas.models.security.SecurityRequirement> securityRequirements = new ArrayList<>();

        for (Map<String, List<String>> map : security) {
            io.swagger.v3.oas.models.security.SecurityRequirement securityRequirement =
                new io.swagger.v3.oas.models.security.SecurityRequirement();

            map.forEach(securityRequirement::addList);

            securityRequirements.add(securityRequirement);
        }

        return securityRequirements;
    }


    private SecurityScheme convertOauth2SecurityScheme(SecuritySchemeDefinition definition) {
        SecurityScheme securityScheme = new SecurityScheme();
        OAuth2Definition oauthToDefinition = (OAuth2Definition) definition;
        OAuthFlows oauthFlows = new OAuthFlows();
        OAuthFlow oauthFlow = new OAuthFlow();

        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        String flow = oauthToDefinition.getFlow();

        if (flow != null) {
            switch (flow) {
                case "implicit":
                    oauthFlow.setAuthorizationUrl(oauthToDefinition.getAuthorizationUrl());
                    oauthFlows.setImplicit(oauthFlow);
                    break;
                case "password":
                    oauthFlow.setTokenUrl(oauthToDefinition.getTokenUrl());
                    oauthFlows.setPassword(oauthFlow);
                    break;
                case "application":
                    oauthFlow.setTokenUrl(oauthToDefinition.getTokenUrl());
                    oauthFlows.setClientCredentials(oauthFlow);
                    break;
                case "accessCode":
                    oauthFlow.setAuthorizationUrl(oauthToDefinition.getAuthorizationUrl());
                    oauthFlow.setTokenUrl(oauthToDefinition.getTokenUrl());
                    oauthFlows.setAuthorizationCode(oauthFlow);
                    break;
                default:
                    break;
            }
        }

        Scopes scopes = new Scopes();
        Map<String, String> oauth2Scopes = oauthToDefinition.getScopes();
        if (oauth2Scopes != null) {
            oauth2Scopes.forEach(scopes::addString);
        }
        oauthFlow.setScopes(scopes);

        securityScheme.setFlows(oauthFlows);

        return securityScheme;
    }

    private SecurityScheme convertApiKeySecurityScheme(SecuritySchemeDefinition definition) {
        SecurityScheme securityScheme = new SecurityScheme();
        ApiKeyAuthDefinition apiKeyAuthDefinition = (ApiKeyAuthDefinition) definition;

        securityScheme.setType(SecurityScheme.Type.APIKEY);
        securityScheme.setName(apiKeyAuthDefinition.getName());
        securityScheme.setIn(SecurityScheme.In.valueOf(apiKeyAuthDefinition.getIn().toString()));

        return securityScheme;
    }

    private SecurityScheme createBasicSecurityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();

        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.setScheme("basic");

        return securityScheme;
    }

    private List<Tag> convertTags(List<io.swagger.models.Tag> v2tags) {
        List<Tag> v3tags = new ArrayList<>();

        for (io.swagger.models.Tag v2tag : v2tags) {
            Tag v3tag = new Tag();

            v3tag.setDescription(v2tag.getDescription());
            v3tag.setName(v2tag.getName());

            if (v2tag.getExternalDocs() != null) {
                v3tag.setExternalDocs(convert(v2tag.getExternalDocs()));
            }

            Map<String, Object> extensions = convert(v2tag.getVendorExtensions());
            if (extensions != null) {
                v3tag.setExtensions(extensions);
            }
            v3tags.add(v3tag);
        }

        return v3tags;
    }


    private boolean isRefABodyParam(io.swagger.models.parameters.Parameter param,
        Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters) {
        if (param instanceof RefParameter) {
            RefParameter refParameter = (RefParameter) param;
            String simpleRef = refParameter.getSimpleRef();
            io.swagger.models.parameters.Parameter parameter = globalV2Parameters.get(simpleRef);
            return "body".equals(parameter.getIn());
        }
        return false;
    }

    private boolean isRefAFormParam(io.swagger.models.parameters.Parameter param,
        Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters) {
        if (param instanceof RefParameter) {
            RefParameter refParameter = (RefParameter) param;
            String simpleRef = refParameter.getSimpleRef();
            io.swagger.models.parameters.Parameter parameter = globalV2Parameters.get(simpleRef);
            return "formData".equals(parameter.getIn());
        }
        return false;
    }


    private Schema convertFormDataToSchema(io.swagger.models.parameters.Parameter formParam) {
        SerializableParameter sp = (SerializableParameter) formParam;
        return convert(sp);
    }

    private RequestBody convertFormDataToRequestBody(List<io.swagger.models.parameters.Parameter> formParams,
        List<String> consumes, List<String> globalConsumes,
        Map<String, io.swagger.models.parameters.Parameter> globalV2Parameters, Components components) {
        final RequestBody body = new RequestBody();

        Schema formSchema = new Schema();

        for (io.swagger.models.parameters.Parameter param : formParams) {
            SerializableParameter sp;

            Schema schema;
            String name;
            if (param instanceof RefParameter) {
                RefParameter refParameter = (RefParameter) param;
                String simpleRef = refParameter.getSimpleRef();
                sp = (SerializableParameter) globalV2Parameters.get(simpleRef);
                name = components.getSchemas().get("formData_" + simpleRef).getName();
                schema = new Schema().$ref("#/components/schemas/formData_" + simpleRef);
            } else {
                sp = (SerializableParameter) param;
                schema = convert(sp);
                name = schema.getName();
            }

            if (sp.getRequired()) {
                formSchema.addRequiredItem(sp.getName());
            }

            formSchema.addProperties(name, schema);
        }

        List<String> mediaTypes = new ArrayList<>(globalConsumes);
        if (consumes != null && consumes.size() > 0) {
            mediaTypes.clear();
            mediaTypes.addAll(consumes);
        }

        // Assume multipart/form-data if nothing is specified
        if (mediaTypes.size() == 0) {
            mediaTypes.add("multipart/form-data");
        }

        Content content = new Content();
        for (String type : mediaTypes) {
            content.addMediaType(type, new MediaType().schema(formSchema));
        }
        body.content(content);
        return body;
    }


    private RequestBody convertParameterToRequestBody(io.swagger.models.parameters.Parameter param,
        List<String> globalConsumes) {
        return convertParameterToRequestBody(param, null, globalConsumes);
    }

    private RequestBody convertParameterToRequestBody(io.swagger.models.parameters.Parameter param,
        List<String> consumes, List<String> globalConsumes) {
        RequestBody body = new RequestBody();
        BodyParameter bp = (BodyParameter) param;

        List<String> mediaTypes = new ArrayList<>(globalConsumes);
        if (consumes != null && consumes.size() > 0) {
            mediaTypes.clear();
            mediaTypes.addAll(consumes);
        }

        if (mediaTypes.size() == 0) {
            mediaTypes.add("*/*");
        }

        if (StringUtils.isNotBlank(param.getDescription())) {
            body.description(param.getDescription());
        }
        body.required(param.getRequired());

        Content content = new Content();
        for (String type : mediaTypes) {
            content.addMediaType(type,
                new MediaType().schema(
                    convert(bp.getSchema())));
            if (StringUtils.isNotBlank(bp.getDescription())) {
                body.setDescription(bp.getDescription());
            }
        }
        convertExamples(((BodyParameter) param).getExamples(), content);
        body.content(content);
        return body;
    }


    private RequestBody convertParametersToRequestBody(List<io.swagger.models.parameters.Parameter> bodyParams,
        List<String> consumes, List<String> globalConsumes) {

        final RequestBody body = new RequestBody();

        Schema bodySchema = new Schema();

        if (bodyParams.size() == 1) {
            BodyParameter sp = (BodyParameter) bodyParams.get(0);
            Schema schema = convert(sp.getSchema());
            if (StringUtils.isBlank(schema.getType()) && StringUtils.isNotBlank(schema.get$ref())) {
                bodySchema = schema;
            } else if (OBJECT_TYPE.contains(SchemaType.resolve(schema.getType(), schema.getFormat()))) {
                bodySchema = schema;
            } else {
                multipleBodySchema(bodyParams, bodySchema);
            }
        } else {
            multipleBodySchema(bodyParams, bodySchema);
        }

        List<String> mediaTypes = new ArrayList<>(globalConsumes);

        if (consumes != null && consumes.size() > 0) {
            mediaTypes.clear();
            mediaTypes.addAll(consumes);
        }

        if (mediaTypes.size() == 0) {
            mediaTypes.add("*/*");
        }

        Content content = new Content();
        for (String type : mediaTypes) {
            content.addMediaType(type, new MediaType().schema(bodySchema));
        }
        body.content(content);
        return body;
    }

    private void multipleBodySchema(List<io.swagger.models.parameters.Parameter> bodyParams, Schema bodySchema) {
        for (io.swagger.models.parameters.Parameter param : bodyParams) {
            BodyParameter sp = (BodyParameter) param;

            Schema schema = convert(sp.getSchema());

            String name = param.getName();
            schema.setName(name);

            if (StringUtils.isNotBlank(param.getDescription())) {
                schema.setDescription(param.getDescription());
            }

            if (sp.getRequired()) {
                bodySchema.addRequiredItem(sp.getName());
            }

            bodySchema.addProperties(name, schema);
        }
    }


    private Content convertExamples(final Map examples, final Content content) {
        if (examples != null) {
            examples.forEach((k, v) -> {
                MediaType mt = content.computeIfAbsent(k.toString(), (key) -> new MediaType());
                mt.setExample(v);
            });
        }
        return content;
    }

    private Schema convertFileSchema(Schema schema) {
        if ("file".equals(schema.getType())) {
            schema.setType("string");
            schema.setFormat("binary");
        }

        return schema;
    }

    private Map<String, Header> convertHeaders(Map<String, Property> headers) {
        Map<String, Header> result = new HashMap<>();

        headers.forEach((k, v) -> result.put(k, convertHeader(v)));

        return result;
    }

    private Header convertHeader(Property property) {
        Schema schema = convert(property);
        schema.setDescription(null);

        Header header = new Header();
        header.setDescription(property.getDescription());
        header.setSchema(schema);

        return header;
    }


    private void addProperties(Model v2Model, Schema schema) {
        if ((v2Model.getProperties() != null) && (v2Model.getProperties().size() > 0)) {
            Map<String, Property> properties = v2Model.getProperties();

            properties.forEach((k, v) -> schema.addProperties(k, convert(v)));

        }
    }

    public static <T> void ifNotNull(T t, Consumer<T> callback) {
        if (Objects.nonNull(t)) {
            callback.accept(t);
        }
    }
}

