package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.ADD_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.GET_PROJECT_ENVIRONMENT_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.GlobalEnvironmentDto;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectEnvironmentDto;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.service.impl.ProjectEnvironmentServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

@DisplayName("Test cases for ProjectEnvironmentService")
class ProjectEnvironmentServiceTest {

    private ProjectEnvironmentRepository projectEnvironmentRepository = mock(ProjectEnvironmentRepository.class);
    private GlobalEnvironmentService globalEnvironmentService = mock(GlobalEnvironmentService.class);
    private ProjectEnvironmentMapper projectEnvironmentMapper = mock(ProjectEnvironmentMapper.class);
    private MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private ProjectEnvironmentService projectEnvironmentService =
        new ProjectEnvironmentServiceImpl(projectEnvironmentRepository, globalEnvironmentService,
            mongoTemplate, projectEnvironmentMapper);

    private final ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().id(ID).build();
    private final ProjectEnvironmentDto projectEnvironmentDto = ProjectEnvironmentDto.builder().id(ID).build();
    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private static final int FRONT_FIRST_NUMBER = 1;
    private final static String ID = "607cebb2fbe52328bf14a2a2";
    private final static String NOT_EXIST_ID = "607cebb223552328bf14a2a2";
    private final static String PROJECT_ID = "25";
    private final static String EVN_NAME = "evnName";

    @Test
    @DisplayName("Test the paging method with no parameters in the ProjectEnvironment service")
    void page_default_test() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironment> example = Example.of(projectEnvironment);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<ProjectEnvironment> projectEnvironmentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentList.add(ProjectEnvironment.builder().envName(EVN_NAME).build());
        }
        Page<ProjectEnvironment> projectEnvironmentPage = new PageImpl<>(projectEnvironmentList, pageable,
            TOTAL_ELEMENTS);
        when(projectEnvironmentRepository.findAll(example, pageable)).thenReturn(projectEnvironmentPage);
        when(projectEnvironmentMapper.toDto(any()))
            .thenReturn(ProjectEnvironmentDto.builder().envName(EVN_NAME).build());
        Page<ProjectEnvironmentDto> projectEnvironmentDtoPage = projectEnvironmentService.page(pageDto, PROJECT_ID);
        assertThat(projectEnvironmentDtoPage.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectEnvironmentDtoPage.getPageable().getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(projectEnvironmentDtoPage.getPageable().getPageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(projectEnvironmentDtoPage.getContent())
            .allMatch(projectDto -> StringUtils.equals(projectDto.getEnvName(),
                EVN_NAME));
    }

    @Test
    @DisplayName("Test the paging method with specified parameters in the ProjectEnvironment service")
    void page_test() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironment> example = Example.of(projectEnvironment);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<ProjectEnvironment> projectEnvironmentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentList.add(ProjectEnvironment.builder().envName(EVN_NAME).build());
        }
        Page<ProjectEnvironment> projectEnvironmentPage = new PageImpl<>(projectEnvironmentList, pageable,
            TOTAL_ELEMENTS);
        when(projectEnvironmentRepository.findAll(example, pageable)).thenReturn(projectEnvironmentPage);
        when(projectEnvironmentMapper.toDto(any()))
            .thenReturn(ProjectEnvironmentDto.builder().envName(EVN_NAME).build());
        Page<ProjectEnvironmentDto> projectEnvironmentDtos = projectEnvironmentService.page(pageDto, PROJECT_ID);
        assertThat(projectEnvironmentDtos.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectEnvironmentDtos.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER - FRONT_FIRST_NUMBER);
        assertThat(projectEnvironmentDtos.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(projectEnvironmentDtos.getContent())
            .allMatch(projectDto -> StringUtils.equals(projectDto.getEnvName(), EVN_NAME));
    }

    @Test
    @DisplayName("Test the add method in the ProjectEnvironment service")
    void add_test() {
        ProjectEnvironmentDto projectEnvironmentDto = ProjectEnvironmentDto.builder().build();
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().build();
        when(projectEnvironmentMapper.toEntity(projectEnvironmentDto)).thenReturn(projectEnvironment);
        when(projectEnvironmentRepository.insert(projectEnvironment)).thenReturn(projectEnvironment);
        projectEnvironmentService.add(projectEnvironmentDto);
        verify(projectEnvironmentRepository, times(1)).insert(any(ProjectEnvironment.class));
    }

    @Test
    @DisplayName("Test the edit method in the ProjectEnvironment service")
    void edit_test() {
        ProjectEnvironmentDto projectEnvironmentDto = ProjectEnvironmentDto.builder().id(ID).build();
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().id(ID).build();
        when(projectEnvironmentMapper.toEntity(projectEnvironmentDto)).thenReturn(projectEnvironment);
        when(projectEnvironmentRepository.findById(ID)).thenReturn(Optional.of(ProjectEnvironment.builder().build()));
        when(projectEnvironmentRepository.save(projectEnvironment)).thenReturn(projectEnvironment);
        projectEnvironmentService.edit(projectEnvironmentDto);
        verify(projectEnvironmentRepository, times(1)).save(any(ProjectEnvironment.class));
    }

    @Test
    @DisplayName("Test the method of querying the ProjectEnvironment by id")
    void findProjectEnvironmentById() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().envName(EVN_NAME).build();
        ProjectEnvironmentDto projectEnvironmentDto = ProjectEnvironmentDto.builder().envName(EVN_NAME).build();
        Optional<ProjectEnvironment> projectEnvironmentOptional = Optional.ofNullable(projectEnvironment);
        when(projectEnvironmentRepository.findById(ID)).thenReturn(projectEnvironmentOptional);
        when(projectEnvironmentMapper.toDto(projectEnvironment)).thenReturn(projectEnvironmentDto);
        ProjectEnvironmentDto result1 = projectEnvironmentService.findById(ID);
        ProjectEnvironmentDto result2 = projectEnvironmentService.findById(NOT_EXIST_ID);
        assertThat(result1.getEnvName()).isEqualTo(EVN_NAME);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("Test the delete method in the ProjectEnvironment service")
    void delete_test() {
        projectEnvironmentService.delete(new String[]{ID});
        verify(mongoTemplate, times(1))
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment page")
    void page_exception_test() {
        ProjectEnvironment projectEnvironment = ProjectEnvironment.builder().projectId(PROJECT_ID).build();
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(
            pageDto.getPageNumber(), pageDto.getPageSize(), sort);
        doThrow(new RuntimeException()).when(projectEnvironmentRepository)
            .findAll(Example.of(projectEnvironment), pageable);
        assertThatThrownBy(() -> projectEnvironmentService.page(pageDto, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_ENVIRONMENT_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while adding ProjectEnvironment")
    void add_exception_test() {
        when(projectEnvironmentMapper.toEntity(any())).thenReturn(ProjectEnvironment.builder().build());
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).insert(any(ProjectEnvironment.class));
        assertThatThrownBy(() -> projectEnvironmentService.add(ProjectEnvironmentDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit ProjectEnvironment")
    void edit_exception_test() {
        when(projectEnvironmentMapper.toEntity(any())).thenReturn(ProjectEnvironment.builder().id(ID).build());
        when(projectEnvironmentRepository.findById(ID)).thenReturn(Optional.of(ProjectEnvironment.builder().build()));
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> projectEnvironmentService.edit(ProjectEnvironmentDto.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment by id")
    void getProjectEnvironment_exception_test() {
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).findById(anyString());
        assertThatThrownBy(() -> projectEnvironmentService.findById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete ProjectEnvironment")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(mongoTemplate)
            .updateMulti(any(Query.class), any(UpdateDefinition.class), any(Class.class));
        assertThatThrownBy(() -> projectEnvironmentService.delete(new String[]{ID}))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ProjectEnvironment service")
    public void list_test() {
        ArrayList<ProjectEnvironment> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ProjectEnvironment.builder().build());
        }
        ArrayList<ProjectEnvironmentDto> projectEnvironmentDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentDtos.add(ProjectEnvironmentDto.builder().build());
        }
        when(projectEnvironmentRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(projectEnvironmentMapper.toDtoList(list)).thenReturn(projectEnvironmentDtos);
        when(globalEnvironmentService.list())
            .thenReturn(Collections.singletonList(GlobalEnvironmentDto.builder().build()));
        List<Object> result = projectEnvironmentService.list(PROJECT_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS + 1);
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment list")
    public void list_exception_test() {
        when(globalEnvironmentService.list()).thenReturn(null);
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> projectEnvironmentService.list(PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_ENVIRONMENT_LIST_ERROR.getCode());
    }
}