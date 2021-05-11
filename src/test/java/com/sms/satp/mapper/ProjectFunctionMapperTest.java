package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.ApplicationTests;
import com.sms.satp.dto.ProjectFunctionDto;
import com.sms.satp.entity.function.ProjectFunction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayName("Tests for ProjectFunctionMapper")
class ProjectFunctionMapperTest {

    private ProjectFunctionMapper projectFunctionMapper = new ProjectFunctionMapperImpl(new ParamInfoMapperImpl());

    private static final Integer SIZE = 10;
    private static final String FUNCTION_CODE = "var a = 1";
    private static final String CREATE_TIME_STRING = "2021-04-27 15:21:21";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the ProjectFunction's entity object to a dto object")
    void entity_to_dto() {
        ProjectFunction projectFunction = ProjectFunction.builder()
            .functionCode(FUNCTION_CODE)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        ProjectFunctionDto projectFunctionDto = projectFunctionMapper.toDto(projectFunction);
        assertThat(projectFunctionDto.getFunctionCode()).isEqualTo(FUNCTION_CODE);
    }

    @Test
    @DisplayName("Test the method for converting an ProjectFunction entity list object to a dto list object")
    void projectFunctionList_to_projectFunctionDtoList() {
        List<ProjectFunction> projectFunctions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            projectFunctions.add(ProjectFunction.builder().functionCode(FUNCTION_CODE).build());
        }
        List<ProjectFunctionDto> projectFunctionDtoList = projectFunctionMapper.toDtoList(projectFunctions);
        assertThat(projectFunctionDtoList).hasSize(SIZE);
        assertThat(projectFunctionDtoList).allMatch(result -> StringUtils.equals(result.getFunctionCode(), FUNCTION_CODE));
    }

    @Test
    @DisplayName("Test the method to convert the ProjectFunction's dto object to a entity object")
    void dto_to_entity() {
        ProjectFunctionDto projectFunctionDto = ProjectFunctionDto.builder()
            .functionCode(FUNCTION_CODE)
            .createDateTime(CREATE_TIME_STRING)
            .build();
        ProjectFunction projectFunction = projectFunctionMapper.toEntity(projectFunctionDto);
        assertThat(projectFunction.getFunctionCode()).isEqualTo(FUNCTION_CODE);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectFunction's entity object to a dto object")
    void null_entity_to_dto() {
        ProjectFunctionDto projectFunctionDto = projectFunctionMapper.toDto(null);
        assertThat(projectFunctionDto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the ProjectFunction's dto object to a entity object")
    void null_dto_to_entity() {
        ProjectFunction projectFunction = projectFunctionMapper.toEntity(null);
        assertThat(projectFunction).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an ProjectFunction entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<ProjectFunctionDto> projectFunctionDtoList = projectFunctionMapper.toDtoList(null);
        assertThat(projectFunctionDtoList).isNull();
    }

}