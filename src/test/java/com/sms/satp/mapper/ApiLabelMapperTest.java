package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.entity.ApiLabel;
import com.sms.satp.dto.ApiLabelDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ApiLabelMapper")
class ApiLabelMapperTest {

    private ApiLabelMapper apiLabelMapper = new ApiLabelMapperImpl();

    private static final Integer SIZE = 10;
    private static final String LABEL_NAME = "testName";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ApiLabel's entity object to a dto object")
    void entity_to_dto() {
        ApiLabel apiLabel = ApiLabel.builder()
            .labelName(LABEL_NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ApiLabelDto apiLabelDto = apiLabelMapper.toDto(apiLabel);
        assertThat(apiLabelDto.getLabelName()).isEqualTo(LABEL_NAME);
    }

    @Test
    @DisplayName("Test the method for converting an ApiLabel entity list object to a dto list object")
    void apiLabelList_to_apiLabelDtoList() {
        List<ApiLabel> apiLabels = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiLabels.add(ApiLabel.builder().labelName(LABEL_NAME).build());
        }
        List<ApiLabelDto> apiLabelDtoList = apiLabelMapper.toDtoList(apiLabels);
        assertThat(apiLabelDtoList).hasSize(SIZE);
        assertThat(apiLabelDtoList).allMatch(result -> StringUtils.equals(result.getLabelName(), LABEL_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ApiLabel's dto object to a entity object")
    void dto_to_entity() {
        ApiLabelDto apiLabelDto = ApiLabelDto.builder()
            .labelName(LABEL_NAME)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        ApiLabel apiLabel = apiLabelMapper.toEntity(apiLabelDto);
        assertThat(apiLabel.getLabelName()).isEqualTo(LABEL_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiLabel's entity object to a dto object")
    void null_entity_to_dto() {
        ApiLabelDto apiLabelDto = apiLabelMapper.toDto(null);
        assertThat(apiLabelDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiLabel's dto object to a entity object")
    void null_dto_to_entity() {
        ApiLabel apiLabel = apiLabelMapper.toEntity(null);
        assertThat(apiLabel).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ApiLabel entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiLabelDto> apiLabelDtoList = apiLabelMapper.toDtoList(null);
        assertThat(apiLabelDtoList).isNull();
    }

}