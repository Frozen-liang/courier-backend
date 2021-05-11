package com.sms.satp.service;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.AddSceneCaseDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiDto;
import com.sms.satp.dto.SceneCaseDto;
import com.sms.satp.dto.SceneCaseSearchDto;
import com.sms.satp.dto.UpdateSceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.impl.SceneCaseServiceImpl;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_PAGE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for SceneCaseServiceTest")
class SceneCaseServiceTest {

    private SceneCaseRepository sceneCaseRepository;
    private CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private SceneCaseMapper sceneCaseMapper;
    private SceneCaseServiceImpl sceneCaseService;
    private SceneCaseApiService sceneCaseApiService;
    private ApplicationEventPublisher applicationEventPublisher;
    private SceneCaseApiLogMapper sceneCaseApiLogMapper;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static Long MOCK_CREATE_USER_ID = 1L;
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;


    @BeforeEach
    void setBean() {
        sceneCaseRepository = mock(SceneCaseRepository.class);
        customizedSceneCaseRepository = mock(CustomizedSceneCaseRepository.class);
        sceneCaseMapper = mock(SceneCaseMapper.class);
        sceneCaseApiService = mock(SceneCaseApiService.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        sceneCaseApiLogMapper = mock(SceneCaseApiLogMapper.class);
        sceneCaseService = new SceneCaseServiceImpl(sceneCaseRepository, customizedSceneCaseRepository,
            sceneCaseMapper, sceneCaseApiService, applicationEventPublisher, sceneCaseApiLogMapper);
    }

    @Test
    @DisplayName("Test the add method in the SceneCase service")
    void add_test() {
        SceneCase sceneCase =
            SceneCase.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCase.class))).thenReturn(sceneCase);
        sceneCaseService.add(AddSceneCaseDto.builder().build());
        verify(sceneCaseRepository, times(1)).insert(any(SceneCase.class));
    }

    @Test
    @DisplayName("Test the add method in the SceneCase service throws exception")
    void add_test_thenThrowException() {
        SceneCase sceneCase =
            SceneCase.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCase.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.add(AddSceneCaseDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the SceneCase service")
    void deleteById_test() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doNothing().when(sceneCaseRepository).deleteById(any());
        List<SceneCaseApi> sceneCaseApiDtoList = Lists.newArrayList(SceneCaseApi.builder().build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiDtoList);
        doNothing().when(sceneCaseApiService).deleteById(any());
        sceneCaseService.deleteById(MOCK_ID);
        verify(sceneCaseRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Test the deleteById method in the SceneCase service throws exception")
    void deleteById_test_thenThrownException() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR)).when(sceneCaseRepository).deleteById(any());
        assertThatThrownBy(() -> sceneCaseService.deleteById(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service")
    void edit_test() {
        SceneCase sceneCase =
            SceneCase.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .remove(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCase> optionalSceneCase = Optional.ofNullable(SceneCase.builder().remove(Boolean.TRUE).build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCase.class))).thenReturn(sceneCase);
        List<SceneCaseApiDto> sceneCaseApiDtoList = Lists
            .newArrayList(SceneCaseApiDto.builder().id(MOCK_ID).build());
        when(sceneCaseApiService.listBySceneCaseId(any(), anyBoolean())).thenReturn(sceneCaseApiDtoList);
        doNothing().when(sceneCaseApiService).edit(any());
        sceneCaseService.edit(UpdateSceneCaseDto.builder().remove(Boolean.FALSE).build());
        verify(sceneCaseRepository, times(1)).save(any(SceneCase.class));
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service throws exception")
    void edit_test_thenThrownException() {
        SceneCase sceneCase =
            SceneCase.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .remove(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCase> optionalSceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCase.class)))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.edit(UpdateSceneCaseDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the page method in the SceneCase service")
    void page_test() {
        List<SceneCase> dtoList = Lists.newArrayList(SceneCase.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<SceneCase> sceneCaseDtoPage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(sceneCaseRepository.findAll(any(), (Pageable) any())).thenReturn(sceneCaseDtoPage);
        Page<SceneCaseDto> pageDto = sceneCaseService.page(PageDto.builder().build(), MOCK_PROJECT_ID);
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the SceneCase service thrown exception")
    void page_test_thenThrownException() {
        when(sceneCaseRepository.findAll(any(), (Pageable) any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_PAGE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.page(PageDto.builder().build(), MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the search method in the SceneCase service")
    void search_test() {
        SceneCaseSearchDto dto = new SceneCaseSearchDto();
        dto.setName(MOCK_NAME);
        List<SceneCase> dtoList = Lists.newArrayList(SceneCase.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<SceneCase> sceneCaseDtoPage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(customizedSceneCaseRepository.search(any(), any())).thenReturn(sceneCaseDtoPage);
        Page<SceneCaseDto> pageDto = sceneCaseService.search(dto, MOCK_PROJECT_ID);
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the search method in the SceneCase service thrown exception")
    void search_test_thenThrownException() {
        SceneCaseSearchDto dto = new SceneCaseSearchDto();
        dto.setName(MOCK_NAME);
        when(customizedSceneCaseRepository.search(any(), any()))
            .thenThrow(new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.search(dto, MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }
}
