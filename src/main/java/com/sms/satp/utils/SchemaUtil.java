package com.sms.satp.utils;

import com.sms.satp.entity.Schema;
import com.sms.satp.entity.test.CaseSchema;
import com.sms.satp.entity.test.CaseSchema.CaseSchemaBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SchemaUtil {

    private SchemaUtil() {
    }

    public static final Function<Schema, CaseSchema> CONVERT_TO_CASE_SCHEMA =
        SchemaUtil::schemaConvert;

    private static CaseSchema schemaConvert(Schema schema) {
        Optional<Schema> schemaOptional = Optional.ofNullable(schema);
        if (schemaOptional.isPresent()) {
            CaseSchemaBuilder caseSchemaBuilder = CaseSchema.builder()
                .type(schemaOptional.map(Schema::getType).orElse(null))
                .properties(null);
            schemaOptional.map(Schema::getItem).ifPresent(
                schemaItem -> caseSchemaBuilder.items(
                    Collections.singletonList(CONVERT_TO_CASE_SCHEMA.apply(schemaItem))));
            schemaOptional.map(Schema::getProperties).ifPresent(properties -> {
                Map<String, CaseSchema> schemaMap = properties.entrySet().stream().collect(
                    Collectors.toMap(Entry::getKey, entry -> CONVERT_TO_CASE_SCHEMA.apply(entry.getValue())));
                caseSchemaBuilder.properties(schemaMap);
            });
            return caseSchemaBuilder.build();
        } else {
            return null;
        }
    }
}