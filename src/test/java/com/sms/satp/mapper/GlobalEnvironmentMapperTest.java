package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.GlobalEnvironmentRequest;
import com.sms.satp.dto.response.GlobalEnvironmentResponse;
import com.sms.satp.entity.env.GlobalEnvironmentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for GlobalEnvironmentMapper")
class GlobalEnvironmentMapperTest {

    private GlobalEnvironmentMapper globalEnvironmentMapper = new GlobalEnvironmentMapperImpl(new ParamInfoMapperImpl());

    private static final Integer SIZE = 10;
    private static final String EVN_NAME = "evnName";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the GlobalEnvironment's entity object to a dto object")
    void entity_to_dto() {
        GlobalEnvironmentEntity globalEnvironment = GlobalEnvironmentEntity.builder()
            .envName(EVN_NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        GlobalEnvironmentResponse globalEnvironmentDto = globalEnvironmentMapper.toDto(globalEnvironment);
        assertThat(globalEnvironmentDto.getEnvName()).isEqualTo(EVN_NAME);
    }

    @Test
    @DisplayName("Test the method for converting an GlobalEnvironment entity list object to a dto list object")
    void globalEnvironmentList_to_globalEnvironmentDtoList() {
        List<GlobalEnvironmentEntity> globalEnvironments = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            globalEnvironments.add(GlobalEnvironmentEntity.builder().envName(EVN_NAME).build());
        }
        List<GlobalEnvironmentResponse> globalEnvironmentDtoList = globalEnvironmentMapper.toDtoList(globalEnvironments);
        assertThat(globalEnvironmentDtoList).hasSize(SIZE);
        assertThat(globalEnvironmentDtoList).allMatch(result -> StringUtils.equals(result.getEnvName(), EVN_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the GlobalEnvironment's dto object to a entity object")
    void dto_to_entity() {
        GlobalEnvironmentRequest globalEnvironmentDto = GlobalEnvironmentRequest.builder()
            .envName(EVN_NAME)
            .build();
        GlobalEnvironmentEntity globalEnvironment = globalEnvironmentMapper.toEntity(globalEnvironmentDto);
        assertThat(globalEnvironment.getEnvName()).isEqualTo(EVN_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the GlobalEnvironment's entity object to a dto object")
    void null_entity_to_dto() {
        GlobalEnvironmentResponse globalEnvironmentDto = globalEnvironmentMapper.toDto(null);
        assertThat(globalEnvironmentDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the GlobalEnvironment's dto object to a entity object")
    void null_dto_to_entity() {
        GlobalEnvironmentEntity globalEnvironment = globalEnvironmentMapper.toEntity(null);
        assertThat(globalEnvironment).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an GlobalEnvironment entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<GlobalEnvironmentResponse> globalEnvironmentDtoList = globalEnvironmentMapper.toDtoList(null);
        assertThat(globalEnvironmentDtoList).isNull();
    }

}