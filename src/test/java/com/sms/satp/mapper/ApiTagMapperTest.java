package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.dto.ApiTagDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiTagMapper")
class ApiTagMapperTest {

    private ApiTagMapper apiTagMapper = new ApiTagMapperImpl();

    private static final Integer SIZE = 10;
    private static final String TAG_NAME = "testName";
    private static final String CREATE_TIME_STRING = "2020-02-10 14:24:35";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ApiTag's entity object to a dto object")
    void entity_to_dto() {
        ApiTag apiTag = ApiTag.builder()
            .tagName(TAG_NAME)
            .tagType(ApiTagType.API)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ApiTagDto apiTagDto = apiTagMapper.toDto(apiTag);
        assertThat(apiTagDto.getTagName()).isEqualTo(TAG_NAME);
        assertThat(apiTagDto.getTagType()).isEqualTo(ApiTagType.API.getCode());
    }

    @Test
    @DisplayName("Test the method for converting an ApiTag entity list object to a dto list object")
    void apiTagList_to_apiTagDtoList() {
        List<ApiTag> apiTags = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiTags.add(ApiTag.builder().tagName(TAG_NAME).tagType(ApiTagType.API).build());
        }
        List<ApiTagDto> apiTagDtoList = apiTagMapper.toDtoList(apiTags);
        assertThat(apiTagDtoList).hasSize(SIZE);
        assertThat(apiTagDtoList).allMatch(result -> StringUtils.equals(result.getTagName(), TAG_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ApiTag's dto object to a entity object")
    void dto_to_entity() {
        ApiTagDto apiTagDto = ApiTagDto.builder()
            .tagName(TAG_NAME)
            .tagType(ApiTagType.CASE.getCode())
            .createDateTime(CREATE_TIME_STRING)
            .build();
        ApiTag apiTag = apiTagMapper.toEntity(apiTagDto);
        assertThat(apiTag.getTagName()).isEqualTo(TAG_NAME);
        assertThat(apiTag.getTagType()).isEqualTo(ApiTagType.CASE);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTag's entity object to a dto object")
    void null_entity_to_dto() {
        ApiTagDto apiTagDto = apiTagMapper.toDto(null);
        assertThat(apiTagDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTag's dto object to a entity object")
    void null_dto_to_entity() {
        ApiTag apiTag = apiTagMapper.toEntity(null);
        assertThat(apiTag).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ApiTag entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiTagDto> apiTagDtoList = apiTagMapper.toDtoList(null);
        assertThat(apiTagDtoList).isNull();
    }

}