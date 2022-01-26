package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for OpenApiSettingMapper")
class OpenApiSettingMapperTest {

    private final OpenApiSettingMapper openApiSettingMapper = new OpenApiSettingMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "openApiSetting";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the OpenApiSetting's entity object to a dto object")
    void entity_to_dto() {
        OpenApiSettingEntity openApiSetting = OpenApiSettingEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        OpenApiSettingResponse openApiSettingResponse = openApiSettingMapper.toDto(openApiSetting);
        assertThat(openApiSettingResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an OpenApiSetting entity list object to a dto list object")
    void openApiSettingList_to_openApiSettingDtoList() {
        List<OpenApiSettingEntity> openApiSettings = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            openApiSettings.add(OpenApiSettingEntity.builder().name(NAME).build());
        }
        List<OpenApiSettingResponse> openApiSettingResponseList = openApiSettingMapper.toDtoList(openApiSettings);
        assertThat(openApiSettingResponseList).hasSize(SIZE);
        assertThat(openApiSettingResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the OpenApiSetting's dto object to a entity object")
    void dto_to_entity() {
        OpenApiSettingRequest openApiSettingRequest = OpenApiSettingRequest.builder()
            .name(NAME)
            .build();
        OpenApiSettingEntity openApiSetting = openApiSettingMapper.toEntity(openApiSettingRequest);
        assertThat(openApiSetting.getName()).isEqualTo(NAME);
    }

}
