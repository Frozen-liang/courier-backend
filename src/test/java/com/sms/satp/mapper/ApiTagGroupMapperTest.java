package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.entity.group.ApiTagGroupEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiTagGroupMapper")
class ApiTagGroupMapperTest {

    private ApiTagGroupMapper apiTagGroupMapper = new ApiTagGroupMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "apiTagGroup";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ApiTagGroup's entity object to a dto object")
    void entity_to_dto() {
        ApiTagGroupEntity apiTagGroup = ApiTagGroupEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ApiTagGroupResponse apiTagGroupResponse = apiTagGroupMapper.toDto(apiTagGroup);
        assertThat(apiTagGroupResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an ApiTagGroup entity list object to a dto list object")
    void apiTagGroupList_to_apiTagGroupResponseList() {
        List<ApiTagGroupEntity> apiTagGroups = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            apiTagGroups.add(ApiTagGroupEntity.builder().name(NAME).build());
        }
        List<ApiTagGroupResponse> apiTagGroupResponseList = apiTagGroupMapper.toDtoList(apiTagGroups);
        assertThat(apiTagGroupResponseList).hasSize(SIZE);
        assertThat(apiTagGroupResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the ApiTagGroup's dto object to a entity object")
    void dto_to_entity() {
        ApiTagGroupRequest apiTagGroupRequest = ApiTagGroupRequest.builder()
            .name(NAME)
            .build();
        ApiTagGroupEntity apiTagGroup = apiTagGroupMapper.toEntity(apiTagGroupRequest);
        assertThat(apiTagGroup.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTagGroup's entity object to a dto object")
    void null_entity_to_dto() {
        ApiTagGroupResponse apiTagGroupResponse = apiTagGroupMapper.toDto(null);
        assertThat(apiTagGroupResponse).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ApiTagGroup's dto object to a entity object")
    void null_dto_to_entity() {
        ApiTagGroupEntity apiTagGroup = apiTagGroupMapper.toEntity(null);
        assertThat(apiTagGroup).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ApiTagGroup entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ApiTagGroupResponse> apiTagGroupResponseList = apiTagGroupMapper.toDtoList(null);
        assertThat(apiTagGroupResponseList).isNull();
    }

}