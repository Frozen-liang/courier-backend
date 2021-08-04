package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_PROJECT_ENVIRONMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_PAGE_ERROR;
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

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ProjectEnvironmentRequest;
import com.sms.courier.dto.response.GlobalEnvironmentResponse;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.mapper.ProjectEnvironmentMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ProjectEnvironmentRepository;
import com.sms.courier.service.impl.ProjectEnvironmentServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DisplayName("Test cases for ProjectEnvironmentService")
class ProjectEnvironmentServiceTest {

    private ProjectEnvironmentRepository projectEnvironmentRepository = mock(ProjectEnvironmentRepository.class);
    private GlobalEnvironmentService globalEnvironmentService = mock(GlobalEnvironmentService.class);
    private ProjectEnvironmentMapper projectEnvironmentMapper = mock(ProjectEnvironmentMapper.class);
    private CommonRepository commonRepository = mock(CommonRepository.class);
    private ProjectEnvironmentService projectEnvironmentService =
        new ProjectEnvironmentServiceImpl(projectEnvironmentRepository, globalEnvironmentService,
            commonRepository, projectEnvironmentMapper);
    private final ProjectEnvironmentRequest projectEnvironmentRequest = ProjectEnvironmentRequest
        .builder().id(ID).build();
    private final static int TOTAL_ELEMENTS = 60;
    private final static int PAGE_NUMBER = 2;
    private final static int PAGE_SIZE = 20;
    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;
    private static final int FRONT_FIRST_NUMBER = 1;
    private final static String ID = "607cebb2fbe52328bf14a2a2";
    private final static String WORKSPACE_ID = ObjectId.get().toString();
    private static final List<String> ID_LIST = Collections.singletonList(ID);
    private final static String PROJECT_ID = "25";
    private final static String EVN_NAME = "evnName";

    @Test
    @DisplayName("Test the paging method with no parameters in the ProjectEnvironment service")
    void page_default_test() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironmentEntity> example = Example.of(projectEnvironment);
        PageDto pageDto = PageDto.builder().build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<ProjectEnvironmentEntity> projectEnvironmentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentList.add(ProjectEnvironmentEntity.builder().envName(EVN_NAME).build());
        }
        Page<ProjectEnvironmentEntity> projectEnvironmentPage = new PageImpl<>(projectEnvironmentList, pageable,
            TOTAL_ELEMENTS);
        when(projectEnvironmentRepository.findAll(example, pageable)).thenReturn(projectEnvironmentPage);
        when(projectEnvironmentMapper.toDto(any()))
            .thenReturn(ProjectEnvironmentResponse.builder().envName(EVN_NAME).build());
        Page<ProjectEnvironmentResponse> projectEnvironmentDtoPage = projectEnvironmentService
            .page(pageDto, PROJECT_ID);
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
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder()
            .projectId(PROJECT_ID)
            .build();
        Example<ProjectEnvironmentEntity> example = Example.of(projectEnvironment);
        PageDto pageDto = PageDto.builder()
            .pageNumber(PAGE_NUMBER)
            .pageSize(PAGE_SIZE)
            .order("asc")
            .build();
        Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
        Pageable pageable = PageRequest.of(pageDto.getPageNumber() - FRONT_FIRST_NUMBER, pageDto.getPageSize(), sort);
        List<ProjectEnvironmentEntity> projectEnvironmentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentList.add(ProjectEnvironmentEntity.builder().envName(EVN_NAME).build());
        }
        Page<ProjectEnvironmentEntity> projectEnvironmentPage = new PageImpl<>(projectEnvironmentList, pageable,
            TOTAL_ELEMENTS);
        when(projectEnvironmentRepository.findAll(example, pageable)).thenReturn(projectEnvironmentPage);
        when(projectEnvironmentMapper.toDto(any()))
            .thenReturn(ProjectEnvironmentResponse.builder().envName(EVN_NAME).build());
        Page<ProjectEnvironmentResponse> projectEnvironmentDtos = projectEnvironmentService.page(pageDto, PROJECT_ID);
        assertThat(projectEnvironmentDtos.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(projectEnvironmentDtos.getPageable().getPageNumber()).isEqualTo(PAGE_NUMBER - FRONT_FIRST_NUMBER);
        assertThat(projectEnvironmentDtos.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);
        assertThat(projectEnvironmentDtos.getContent())
            .allMatch(projectDto -> StringUtils.equals(projectDto.getEnvName(), EVN_NAME));
    }

    @Test
    @DisplayName("Test the add method in the ProjectEnvironment service")
    void add_test() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().build();
        when(projectEnvironmentMapper.toEntity(projectEnvironmentRequest)).thenReturn(projectEnvironment);
        when(projectEnvironmentRepository.insert(projectEnvironment)).thenReturn(projectEnvironment);
        projectEnvironmentService.add(projectEnvironmentRequest);
        verify(projectEnvironmentRepository, times(1)).insert(any(ProjectEnvironmentEntity.class));
    }

    @Test
    @DisplayName("Test the edit method in the ProjectEnvironment service")
    void edit_test() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().id(ID).build();
        when(projectEnvironmentMapper.toEntity(projectEnvironmentRequest)).thenReturn(projectEnvironment);
        when(projectEnvironmentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(projectEnvironmentRepository.save(projectEnvironment)).thenReturn(projectEnvironment);
        assertThat(projectEnvironmentService.edit(projectEnvironmentRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the method of querying the ProjectEnvironment by id")
    void findProjectEnvironmentById() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().envName(EVN_NAME).build();
        ProjectEnvironmentResponse projectEnvironmentResponse = ProjectEnvironmentResponse
            .builder().envName(EVN_NAME).build();
        Optional<ProjectEnvironmentEntity> projectEnvironmentOptional = Optional.ofNullable(projectEnvironment);
        when(projectEnvironmentRepository.findById(ID)).thenReturn(projectEnvironmentOptional);
        when(projectEnvironmentMapper.toDto(projectEnvironment)).thenReturn(projectEnvironmentResponse);
        ProjectEnvironmentResponse result1 = projectEnvironmentService.findById(ID);
        assertThat(result1.getEnvName()).isEqualTo(EVN_NAME);
    }

    @Test
    @DisplayName("Test the delete method in the ProjectEnvironment service")
    void delete_test() {
        when(commonRepository.deleteByIds(ID_LIST, ProjectEnvironmentEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(projectEnvironmentService.delete(ID_LIST)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment page")
    void page_exception_test() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().projectId(PROJECT_ID).build();
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
        when(projectEnvironmentMapper.toEntity(any())).thenReturn(ProjectEnvironmentEntity.builder().build());
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).insert(any(ProjectEnvironmentEntity.class));
        assertThatThrownBy(() -> projectEnvironmentService.add(ProjectEnvironmentRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_PROJECT_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while edit ProjectEnvironment")
    void edit_exception_test() {
        when(projectEnvironmentMapper.toEntity(any())).thenReturn(ProjectEnvironmentEntity.builder().id(ID).build());
        when(projectEnvironmentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).save(argThat(t -> true));
        assertThatThrownBy(() -> projectEnvironmentService.edit(ProjectEnvironmentRequest.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_PROJECT_ENVIRONMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ProjectEnvironment")
    public void edit_not_exist_exception_test() {
        when(projectEnvironmentMapper.toEntity(any())).thenReturn(ProjectEnvironmentEntity.builder().id(ID).build());
        when(projectEnvironmentRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> projectEnvironmentService.edit(ProjectEnvironmentRequest.builder().id(ID).build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment by id")
    void getProjectEnvironment_exception_test() {
        when(projectEnvironmentRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> projectEnvironmentService.findById(anyString()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while delete ProjectEnvironment")
    void delete_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository)
            .deleteByIds(ID_LIST, ProjectEnvironmentEntity.class);
        assertThatThrownBy(() -> projectEnvironmentService.delete(ID_LIST))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_PROJECT_ENVIRONMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ProjectEnvironment service")
    public void list_test() {
        ArrayList<ProjectEnvironmentEntity> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ProjectEnvironmentEntity.builder().build());
        }
        ArrayList<ProjectEnvironmentResponse> projectEnvironmentDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            projectEnvironmentDtos.add(ProjectEnvironmentResponse.builder().build());
        }
        when(projectEnvironmentRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(projectEnvironmentMapper.toDtoList(list)).thenReturn(projectEnvironmentDtos);
        when(globalEnvironmentService.list(WORKSPACE_ID))
            .thenReturn(Collections.singletonList(GlobalEnvironmentResponse.builder().build()));
        List<Object> result = projectEnvironmentService.list(PROJECT_ID,WORKSPACE_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS + 1);
    }

    @Test
    @DisplayName("An exception occurred while getting ProjectEnvironment list")
    public void list_exception_test() {
        when(globalEnvironmentService.list(WORKSPACE_ID)).thenReturn(null);
        doThrow(new RuntimeException()).when(projectEnvironmentRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> projectEnvironmentService.list(PROJECT_ID,WORKSPACE_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_PROJECT_ENVIRONMENT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the method of find the ProjectEnvironment by id")
    void findOne_test() {
        ProjectEnvironmentEntity projectEnvironment = ProjectEnvironmentEntity.builder().envName(EVN_NAME).build();
        Optional<ProjectEnvironmentEntity> projectEnvironmentOptional = Optional.ofNullable(projectEnvironment);
        when(projectEnvironmentRepository.findById(ID)).thenReturn(projectEnvironmentOptional);
        ProjectEnvironmentEntity result = projectEnvironmentService.findOne(ID);
        assertThat(result.getEnvName()).isEqualTo(EVN_NAME);
    }
}