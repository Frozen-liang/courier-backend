package com.sms.satp.utils;

import static com.sms.satp.utils.ApiRequestBodyConverter.CONVERT_TO_REQUEST_BODY;
import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.RequestBody;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.parser.model.ApiRequestBody;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiRequestBodyConverterTest {

    private static final String NAME =  "name";
    private static final String NAME2 =  "name2-1";
    private static final String NAME3 =  "name2-2";
    private static final String TITLE =  "title";
    private static final String TITLE2 =  "title2-1";
    private static final String TITLE3 =  "title2-2";
    private static final String DESCRIPTION =  "description";
    private static final List<MediaType> MEDIA_TYPE_LIST =
        Arrays.asList(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON);

    @Test
    @DisplayName("Convert the ApiRequestBody to an RequestBody")
    void test_CONVERT_TO_REQUEST_BODY() {
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
        ApiRequestBody apiRequestBody = ApiRequestBody.builder()
            .mediaTypes(MEDIA_TYPE_LIST)
            .schema(apiSchema)
            .build();
        RequestBody requestBody = CONVERT_TO_REQUEST_BODY.apply(apiRequestBody);
        assertThat(requestBody.getSchema().getDescription()).isEqualTo(apiRequestBody.getSchema().getDescription());
    }

    @Test
    @DisplayName("[Null Input Parameter]Convert the ApiRequestBody to an RequestBody")
    void test_Null_CONVERT_TO_REQUEST_BODY() {
        RequestBody requestBody = CONVERT_TO_REQUEST_BODY.apply(null);
        assertThat(requestBody).isNull();
    }
}