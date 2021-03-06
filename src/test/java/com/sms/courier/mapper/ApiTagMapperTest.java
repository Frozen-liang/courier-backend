package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.request.ApiTagListRequest;
import com.sms.courier.dto.request.ApiTagRequest;
import com.sms.courier.dto.response.ApiTagResponse;
import com.sms.courier.entity.tag.ApiTagEntity;
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
        ApiTagEntity apiTag = ApiTagEntity.builder()
            .tagName(TAG_NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ApiTagResponse apiTagDto = apiTagMapper.toDto(apiTag);
        assertThat(apiTagDto.getTagName()).isEqualTo(TAG_NAME);
    }

    @Test
    @DisplayName("Test the method for converting an ApiTag entity list object to a dto list object")
    void apiTagList_to_apiTagDtoList() {
        List<ApiTagEntity> apiTags = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiTags.add(ApiTagEntity.builder().tagName(TAG_NAME).build());
        }
        List<ApiTagResponse> apiTagDtoList = apiTagMapper.toDtoList(apiTags);
        assertThat(apiTagDtoList).hasSize(SIZE);
        assertThat(apiTagDtoList).allMatch(result -> StringUtils.equals(result.getTagName(), TAG_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ApiTag's dto object to a entity object")
    void dto_to_entity() {
        ApiTagRequest apiTagDto = ApiTagRequest.builder()
            .tagName(TAG_NAME)

            .build();
        ApiTagEntity apiTag = apiTagMapper.toEntity(apiTagDto);
        assertThat(apiTag.getTagName()).isEqualTo(TAG_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTag's entity object to a dto object")
    void null_entity_to_dto() {
        ApiTagResponse apiTagDto = apiTagMapper.toDto(null);
        assertThat(apiTagDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTag's dto object to a entity object")
    void null_dto_to_entity() {
        ApiTagEntity apiTag = apiTagMapper.toEntity(null);
        assertThat(apiTag).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ApiTag entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiTagResponse> apiTagDtoList = apiTagMapper.toDtoList(null);
        assertThat(apiTagDtoList).isNull();
    }

    @Test
    @DisplayName("Test the method list request ApiTag object")
    void ApiTagEntity_to_listRequestToApiTag(){
        ApiTagListRequest apiTagListRequest=ApiTagListRequest.builder()
                .projectId("1")
                .tagType(0)
                .tagName("1")
                .build();
        assertThat(apiTagMapper.listRequestToApiTag(apiTagListRequest)).isNotNull();

    }
    @Test
    @DisplayName("Test the method list is null request ApiTag object")
    void null_to_listRequestToApiTag(){
        assertThat(apiTagMapper.listRequestToApiTag(null)).isNull();
    }
}