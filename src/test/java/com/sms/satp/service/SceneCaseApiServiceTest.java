package com.sms.satp.service;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiSortOrderDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.service.impl.SceneCaseApiServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for SceneCaseApiServiceTest")
class SceneCaseApiServiceTest {

    private SceneCaseApiRepository sceneCaseApiRepository;
    private SceneCaseApiMapper sceneCaseApiMapper;
    private SceneCaseApiServiceImpl sceneCaseApiService;

    private final static String MOCK_SCENE_CASE_ID = "1";
    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_ORDER_NUMBER = 1;

    @BeforeEach
    void setUpBean() {
        sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
        sceneCaseApiMapper = mock(SceneCaseApiMapper.class);
        sceneCaseApiService = new SceneCaseApiServiceImpl(sceneCaseApiRepository, sceneCaseApiMapper);
    }

    @Test
    @DisplayName("Test the batch method in the SceneCaseApi service")
    void batch_test() {
        AddSceneCaseApiDto addSceneCaseApiDto = getAddDto();
        SceneCaseApi caseApi = SceneCaseApi.builder().build();
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(sceneCaseApiMapper.toSceneCaseApiList(any())).thenReturn(sceneCaseApiList);
        when(sceneCaseApiRepository.insert(any(List.class))).thenReturn(sceneCaseApiList);
        sceneCaseApiService.batch(addSceneCaseApiDto);
        verify(sceneCaseApiRepository, times(1)).insert(any(List.class));
    }

    @Test
    @DisplayName("Test the batch method in the SceneCaseApi service throws exception")
    void batch_test_thenThrowException() {
        AddSceneCaseApiDto addSceneCaseApiDto = getAddDto();
        SceneCaseApi caseApi = SceneCaseApi.builder().build();
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(sceneCaseApiMapper.toSceneCaseApiList(any())).thenReturn(sceneCaseApiList);
        when(sceneCaseApiRepository.insert(any(List.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.batch(addSceneCaseApiDto))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batch method in the SceneCaseApi service")
    void deleteById_test() {
        doNothing().when(sceneCaseApiRepository).deleteById(any());
        sceneCaseApiService.deleteById(MOCK_ID);
        verify(sceneCaseApiRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Test the batch method in the SceneCaseApi service throws exception")
    void deleteById_test_thenThrowException() {
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR)).when(sceneCaseApiRepository)
            .deleteById(any());
        assertThatThrownBy(() -> sceneCaseApiService.deleteById(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseApi service")
    void edit_test() {
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApi(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.save(any())).thenReturn(sceneCaseApi);
        sceneCaseApiService.edit(SceneCaseApiDto.builder().build());
        verify(sceneCaseApiRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseApi service throws exception")
    void edit_thenThrowException() {
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApi(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.edit(SceneCaseApiDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editSortOrder method in the SceneCaseApi service")
    void batchEdit_test() {
        UpdateSceneCaseApiSortOrderDto dto = getUpdateSortOrder();
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).build();
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any())).thenReturn(sceneCaseApiList);
        sceneCaseApiService.batchEdit(dto);
        verify(sceneCaseApiRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the editSortOrder method in the SceneCaseApi service throws exception")
    void batchEdit_thenThrowException() {
        UpdateSceneCaseApiSortOrderDto dto = getUpdateSortOrder();
        when(sceneCaseApiRepository.saveAll(any()))
            .thenThrow(new ApiTestPlatformException(BATCH_EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.batchEdit(dto)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list method in the SceneCaseApi service")
    void listBySceneCaseId_test() {
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList(SceneCaseApi.builder().build());
        when(sceneCaseApiRepository.findAll(any(), any(Sort.class))).thenReturn(sceneCaseApiList);
        SceneCaseApiDto dto = SceneCaseApiDto.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiDto(any())).thenReturn(dto);
        List<SceneCaseApiDto> dtoList = sceneCaseApiService
            .listBySceneCaseId(MOCK_SCENE_CASE_ID, Boolean.FALSE);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the SceneCaseApi service throws exception")
    void listBySceneCaseId_thenThrowException() {
        when(sceneCaseApiRepository.findAll(any(), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.listBySceneCaseId(MOCK_SCENE_CASE_ID, Boolean.FALSE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list by sceneCaseId method in the SceneCaseApi service")
    void listBySceneCaseId_test_thenReturnSceneCaseApi() {
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList(SceneCaseApi.builder().build());
        when(sceneCaseApiRepository.findAll(any(Example.class))).thenReturn(sceneCaseApiList);
        List<SceneCaseApi> dto = sceneCaseApiService.listBySceneCaseId(MOCK_SCENE_CASE_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list by sceneCaseId method in the SceneCaseApi service")
    void listBySceneCaseId_test_thenThrowException() {
        when(sceneCaseApiRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.listBySceneCaseId(MOCK_SCENE_CASE_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getSceneCaseApiById method in the SceneCaseApi service")
    void getSceneCaseApiById_test() {
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).build();
        Optional<SceneCaseApi> sceneCaseApiOptional = Optional.ofNullable(sceneCaseApi);
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApiOptional);
        SceneCaseApiDto sceneCaseApiDto = SceneCaseApiDto.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiDto(any())).thenReturn(sceneCaseApiDto);
        SceneCaseApiDto dto = sceneCaseApiService.getSceneCaseApiById(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getSceneCaseApiById method in the SceneCaseApi service throws exception")
    void getSceneCaseApiById_testThrowException() {
        when(sceneCaseApiRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.getSceneCaseApiById(MOCK_ID));
    }

    private UpdateSceneCaseApiSortOrderDto getUpdateSortOrder() {
        return UpdateSceneCaseApiSortOrderDto.builder()
            .sceneCaseApiDtoList(Lists.newArrayList(SceneCaseApiDto.builder()
                .sceneCaseId(MOCK_SCENE_CASE_ID)
                .orderNumber(MOCK_ORDER_NUMBER)
                .build())).build();
    }

    private AddSceneCaseApiDto getAddDto() {
        return AddSceneCaseApiDto.builder()
            .sceneCaseApiList(Lists.newArrayList(SceneCaseApiDto.builder().build()))
            .build();
    }

}