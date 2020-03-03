package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.entity.Response;
import com.sms.satp.parser.common.MediaType;
import com.sms.satp.parser.common.SchemaType;
import com.sms.satp.parser.model.ApiHeader;
import com.sms.satp.parser.model.ApiResponse;
import com.sms.satp.parser.schema.ApiSchema;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiResponseConverterTest {

    private static final String NAME =  "name";
    private static final String NAME2 =  "name2-1";
    private static final String NAME3 =  "name2-2";
    private static final String API_HEADER_NAME =  "api header name";
    private static final String TITLE =  "title";
    private static final String TITLE2 =  "title2-1";
    private static final String TITLE3 =  "title2-2";
    private static final String DESCRIPTION =  "description";
    private static final String API_HEADER_DESCRIPTION =  "api header description";
    private static final List<ApiHeader> API_HEADER_LIST =
        Collections.singletonList(ApiHeader.builder()
            .name(API_HEADER_NAME).description(API_HEADER_DESCRIPTION).build());
    private static final List<MediaType> MEDIA_TYPE_LIST =
        Arrays.asList(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON);


    @Test
    @DisplayName("Convert the ApiResponse to an Response")
    void test_CONVERT_TO_RESPONSE() {
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
        ApiResponse apiResponse = ApiResponse.builder()
            .headers(API_HEADER_LIST)
            .mediaTypes(MEDIA_TYPE_LIST)
            .schema(apiSchema)
            .build();
        Response response = ApiResponseConverter.CONVERT_TO_RESPONSE.apply(apiResponse);
        assertThat(response.getSchema().getDescription()).isEqualTo(apiResponse.getSchema().getDescription());
    }

    @Test
    @DisplayName("[Null Input Parameter]Convert the ApiResponse to an Response")
    void null_input_test() {
        Response response = ApiResponseConverter.CONVERT_TO_RESPONSE.apply(null);
        assertThat(response).isNull();
    }
}