package com.sms.satp.utils;

import static com.sms.satp.utils.ApiSchemaUtil.CONVERT_TO_SCHEMA;
import static com.sms.satp.utils.ApiSchemaUtil.getRefKey;
import static com.sms.satp.utils.ApiSchemaUtil.resolveApiSchemaMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import com.sms.satp.entity.Schema;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiSchemaUtilTest {

    private static final String NAME =  "name";
    private static final String NAME2 =  "name2-1";
    private static final String NAME3 =  "name2-2";
    private static final String TITLE =  "title";
    private static final String TITLE2 =  "title2-1";
    private static final String TITLE3 =  "title2-2";
    private static final String DESCRIPTION =  "description";

    @Test
    @DisplayName("Convert the ApiSchema to an Schema")
    void test_CONVERT_TO_SCHEMA() {
        Map<String, ApiSchema> properties = new HashMap<>();
        properties.put(NAME2, ApiSchema.builder()
            .name(NAME2)
            .title(TITLE2)
            .type(SchemaType.INT)
            .build());
        properties.put(NAME3, ApiSchema.builder()
            .name(NAME3)
            .title(TITLE3)
            .type(SchemaType.STRING)
            .build());
        ApiSchema apiSchema = ApiSchema.builder()
            .name(NAME)
            .title(TITLE)
            .type(SchemaType.OBJECT)
            .properties(properties)
            .description(DESCRIPTION)
            .build();
        Schema schema = CONVERT_TO_SCHEMA.apply(apiSchema);
        assertThat(StringUtils.equals(schema.getName(), apiSchema.getName()));
        assertThat(StringUtils.equals(schema.getTitle(), apiSchema.getTitle()));
        assertThat(StringUtils.equals(schema.getType().getType(), apiSchema.getType().getType()));
    }

    @Test
    @DisplayName("Gets the reference object in the ref character string")
    void test_GET_REF_KEY() {
        String ref = "#/components/schemas/Category";
        String result = getRefKey(ref);
        assertThat(StringUtils.equals(result, "Category"));
    }

    @Test
    @DisplayName("Throws an ApiTestPlatformException when the parameter is null [GET_REF_KEY]")
    void test_GET_REF_KEY_Error() {
        assertThatThrownBy(() -> getRefKey(null))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ErrorCode.GET_REF_KEY_ERROR.getCode());
    }

    @Test
    @DisplayName("Resolve references in the ApiSchema")
    void test_resolveApiSchemaMap() {
        Map<String, ApiSchema> apiSchemaMap = new HashMap<>();
        Map<String, ApiSchema> categoryProperties = new HashMap<>();
        Map<String, ApiSchema> petProperties = new HashMap<>();
        categoryProperties.put("name", ApiSchema.builder()
            .type(SchemaType.STRING)
            .build());
        categoryProperties.put("id", ApiSchema.builder()
            .type(SchemaType.INT)
            .build());
        petProperties.put("status", ApiSchema.builder()
            .type(SchemaType.STRING)
            .build());
        petProperties.put("id", ApiSchema.builder()
            .type(SchemaType.INT)
            .build());
        petProperties.put("category", ApiSchema.builder()
            .ref("#/components/schemas/Category")
            .build());
        ApiSchema category = ApiSchema.builder()
            .type(SchemaType.OBJECT)
            .properties(categoryProperties)
            .build();
        ApiSchema pet = ApiSchema.builder()
            .type(SchemaType.OBJECT)
            .properties(petProperties)
            .build();
        apiSchemaMap.put("Pet", pet);
        apiSchemaMap.put("Category", category);
        resolveApiSchemaMap(apiSchemaMap, apiSchemaMap);
        ApiSchema categoryPropertiesAfterResolve = apiSchemaMap.get("Pet").getProperties().get("category");
        ApiSchema categorySchemaAfterResolve = apiSchemaMap.get("Category");
        assertThat(categoryPropertiesAfterResolve.getProperties().equals(categorySchemaAfterResolve.getProperties())).isEqualTo(true);
    }
}