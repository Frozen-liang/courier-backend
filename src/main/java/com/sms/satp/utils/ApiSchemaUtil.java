package com.sms.satp.utils;

import com.google.common.base.Splitter;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import com.sms.satp.entity.Schema;
import com.sms.satp.entity.Schema.SchemaBuilder;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;

@Slf4j
public abstract class ApiSchemaUtil {

    private ApiSchemaUtil() {
    }

    public static void removeSchemaMapRef(Map<String, ApiSchema> apiSchemaMap) {
        ApiSchemaMapUtil apiSchemaMapUtil = new ApiSchemaMapUtil(apiSchemaMap);
        apiSchemaMapUtil.removeRef();
    }

    public static String splitKeyFromRef(String ref) {
        if (StringUtils.isNoneBlank(ref)) {
            List<String> refs = Splitter.on("/").splitToList(ref);
            return refs.get(refs.size() - 1);
        } else {
            throw new ApiTestPlatformException(ErrorCode.GET_REF_KEY_ERROR);
        }
    }

    public static final Function<ApiSchema, Schema> CONVERT_TO_SCHEMA =
        ApiSchemaUtil::schemaConvert;

    private static Schema schemaConvert(ApiSchema apiSchema) {
        Optional<ApiSchema> apiSchemaOptional = Optional.ofNullable(apiSchema);
        if (apiSchemaOptional.isPresent()) {
            SchemaBuilder schemaBuilder = Schema.builder()
                .id(new ObjectId().toString())
                .name(apiSchemaOptional.map(ApiSchema::getName).orElse(null))
                .title(apiSchemaOptional.map(ApiSchema::getTitle).orElse(null))
                .required(apiSchemaOptional.map(ApiSchema::getRequired).orElse(null))
                .type(apiSchemaOptional.map(ApiSchema::getType).orElse(null))
                .properties(null)
                .description(apiSchemaOptional.map(ApiSchema::getDescription).orElse(null))
                .deprecated(apiSchemaOptional.map(ApiSchema::getDeprecated).orElse(null));
            apiSchemaOptional.map(ApiSchema::getItem).ifPresent(
                apiSchemaItem -> schemaBuilder.item(CONVERT_TO_SCHEMA.apply(apiSchemaItem)));
            apiSchemaOptional.map(ApiSchema::getProperties).ifPresent(properties -> {
                Map<String, Schema> schemaMap = properties.entrySet().stream().collect(
                    Collectors.toMap(Entry::getKey, entry -> CONVERT_TO_SCHEMA.apply(entry.getValue())));
                schemaBuilder.properties(schemaMap);
            });
            return schemaBuilder.build();
        } else {
            return null;
        }
    }
}