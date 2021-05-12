package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.GlobalFunctionRequest;
import com.sms.satp.dto.response.GlobalFunctionResponse;
import com.sms.satp.entity.function.GlobalFunction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for GlobalFunctionMapper")
class GlobalFunctionMapperTest {

    private GlobalFunctionMapper globalFunctionMapper = new GlobalFunctionMapperImpl(new ParamInfoMapperImpl());

    private static final Integer SIZE = 10;
    private static final String FUNCTION_NAME = "globalFunction";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the GlobalFunction's entity object to a dto object")
    void entity_to_dto() {
        GlobalFunction globalFunction = GlobalFunction.builder()
            .functionName(FUNCTION_NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        GlobalFunctionResponse globalFunctionDto = globalFunctionMapper.toDto(globalFunction);
        assertThat(globalFunctionDto.getFunctionName()).isEqualTo(FUNCTION_NAME);
    }

    @Test
    @DisplayName("Test the method for converting an GlobalFunction entity list object to a dto list object")
    void globalFunctionList_to_globalFunctionDtoList() {
        List<GlobalFunction> globalFunctions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            globalFunctions.add(GlobalFunction.builder().functionName(FUNCTION_NAME).build());
        }
        List<GlobalFunctionResponse> globalFunctionDtoList = globalFunctionMapper.toDtoList(globalFunctions);
        assertThat(globalFunctionDtoList).hasSize(SIZE);
        assertThat(globalFunctionDtoList).allMatch(result -> StringUtils.equals(result.getFunctionName(), FUNCTION_NAME));
    }

    @Test
    @DisplayName("Test the method to convert the GlobalFunction's dto object to a entity object")
    void dto_to_entity() {
        GlobalFunctionRequest globalFunctionDto = GlobalFunctionRequest.builder()
            .functionName(FUNCTION_NAME)
            .build();
        GlobalFunction globalFunction = globalFunctionMapper.toEntity(globalFunctionDto);
        assertThat(globalFunction.getFunctionName()).isEqualTo(FUNCTION_NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the GlobalFunction's entity object to a dto object")
    void null_entity_to_dto() {
        GlobalFunctionResponse globalFunctionDto = globalFunctionMapper.toDto(null);
        assertThat(globalFunctionDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the GlobalFunction's dto object to a entity object")
    void null_dto_to_entity() {
        GlobalFunction globalFunction = globalFunctionMapper.toEntity(null);
        assertThat(globalFunction).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an GlobalFunction entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<GlobalFunctionResponse> globalFunctionDtoList = globalFunctionMapper.toDtoList(null);
        assertThat(globalFunctionDtoList).isNull();
    }

}