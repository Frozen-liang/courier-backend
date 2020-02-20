package com.sms.satp.utils;

import com.google.common.base.Splitter;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import com.sms.satp.entity.Schema;
import com.sms.satp.entity.Schema.SchemaBuilder;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public abstract class ApiSchemaUtil {

    private ApiSchemaUtil() {
    }

    public static void resolveApiSchemaMap(
        Map<String, ApiSchema> apiSchemaMap, Map<String, ApiSchema> root) {
        Iterator<Entry<String, ApiSchema>> entryIterator = apiSchemaMap.entrySet().iterator();
        entryIterator.forEachRemaining(entry -> {
            if (StringUtils.isBlank(entry.getValue().getRef())) {
                if (entry.getValue().getType().matches(SchemaType.OBJECT.getType())) {
                    resolveApiSchemaMap(entry.getValue().getProperties(), root);
                }
            } else {
                String refKey = getRefKey(entry.getValue().getRef());
                entry.setValue(root.get(refKey));
            }
        });
    }

    public static String getRefKey(String ref) {
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
                .name(apiSchemaOptional.map(ApiSchema::getName).orElse(null))
                .title(apiSchemaOptional.map(ApiSchema::getTitle).orElse(null))
                .required(apiSchemaOptional.map(ApiSchema::getRequired).orElse(null))
                .type(apiSchemaOptional.map(ApiSchema::getType).orElse(null))
                .properties(null)
                .description(apiSchemaOptional.map(ApiSchema::getDescription).orElse(null))
                .deprecated(apiSchemaOptional.map(ApiSchema::getDeprecated).orElse(null));
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