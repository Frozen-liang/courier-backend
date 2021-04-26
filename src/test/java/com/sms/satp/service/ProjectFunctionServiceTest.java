package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_FUNCTION_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_FUNCTION_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_FUNCTION_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.ProjectFunctionDto;
import com.sms.satp.entity.function.ProjectFunction;
import com.sms.satp.mapper.ProjectFunctionMapper;
import com.sms.satp.repository.ProjectFunctionRepository;
import com.sms.satp.service.impl.ProjectFunctionServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

@DisplayName("Tests for ProjectFunctionService")
class ProjectFunctionServiceTest {

    private final ProjectFunctionRepository projectFunctionRepository = mock(ProjectFunctionRepository.class);
    private final ProjectFunctionMapper projectFunctionMapper = mock(ProjectFunctionMapper.class);
    private final GlobalFunctionService globalFunctionService = mock(GlobalFunctionService.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final ProjectFunctionService projectFunctionService = new ProjectFunctionServiceImpl(
        projectFunctionRepository, projectFunctionMapper, globalFunctionService, mongoTemplate);
    private final ProjectFunction projectFunction = ProjectFunction.builder().id(ID).build();
    private final ProjectFunctionDto projectFunctionDto = ProjectFunctionDto.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String NOT_EXIST_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = "10";
    private static final String FUNCTION_NAME = "functionName";
    private static final String FUNCTION_DESC = "functionDesc";

    @Test
    @DisplayName("Test the findById method in the ProjectFunction service")
    public void findById_test() {
        when(projectFunctionRepository.findById(ID)).thenReturn(Optional.of(projectFunction));
        when(projectFunctionMapper.toDto(projectFunction)).thenReturn(projectFunctionDto);
        ProjectFunctionDto result1 = projectFunctionService.findById(ID);
        ProjectFunctionDto result2 = projectFunctionService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID.toString());
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectFunction")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(projectFunctionRepository).findById(ID);
        assertThatThrownBy(() -> projectFunctionService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_FUNCTION_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ProjectFunction service")
    public void add_test() {
        when(projectFunctionMapper.toEntity(projectFunctionDto)).thenReturn(projectFunction);
        when(projectFunctionRepository.insert(any(ProjectFunction.class))).thenReturn(projectFunction);
        projectFunctionService.add(projectFunctionDto);
        verify(projectFunctionRepository, times(1)).insert(any(ProjectFunction.class));
    }

    @Test
    @DisplayName("An exception occurred while adding ProjectFunction")
    public void add_exception_test() {
        when(projectFunctionMapper.toEntity(any())).thenReturn(ProjectFunction.builder().build());
        doThrow(new RuntimeException()).when(projectFunctionRepository).insert(any(ProjectFunction.class));
        assertThatThrownBy(() -> projectFunctionService.add(projectFunctionDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ProjectFunction service")
    public void edit_test() {
        when(projectFunctionMapper.toEntity(projectFunctionDto)).thenReturn(projectFunction);
        when(projectFunctionRepository.findById(any()))
            .thenReturn(Optional.of(ProjectFunction.builder().id(ID).build()));
        when(projectFunctionRepository.save(any(ProjectFunction.class))).thenReturn(projectFunction);
        projectFunctionService.edit(projectFunctionDto);
        verify(projectFunctionRepository, times(1)).save(any(ProjectFunction.class));
    }

    @Test
    @DisplayName("An exception occurred while edit ProjectFunction")
    public void edit_exception_test() {
        when(projectFunctionMapper.toEntity(projectFunctionDto)).thenReturn(projectFunction);
        when(projectFunctionRepository.findById(any())).thenReturn(Optional.of(projectFunction));
        doThrow(new RuntimeException()).when(projectFunctionRepository).save(any(ProjectFunction.class));
        assertThatThrownBy(() -> projectFunctionService.edit(projectFunctionDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_FUNCTION_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ProjectFunction service")
    public void list_test() {
        ArrayList<ProjectFunction> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ProjectFunction.builder().build());
        }
        when(projectFunctionRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(projectFunctionMapper.toDto(projectFunction)).thenReturn(projectFunctionDto);
        List<Object> result = projectFunctionService.list(PROJECT_ID, FUNCTION_NAME, FUNCTION_DESC);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectFunction list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(projectFunctionRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> projectFunctionService.list(PROJECT_ID, FUNCTION_NAME, FUNCTION_DESC))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_FUNCTION_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ProjectFunction service")
    public void delete_test() {
        projectFunctionService.delete(new String[]{ID});
        verify(mongoTemplate, times(1))
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
    }

    @Test
    @DisplayName("An exception occurred while delete ProjectFunction")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(mongoTemplate)
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
        assertThatThrownBy(() -> projectFunctionService.delete(new String[]{ID}))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_FUNCTION_BY_ID_ERROR.getCode());
    }
}