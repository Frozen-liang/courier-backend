package com.sms.courier.service;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AddSceneCaseApiRequest;
import com.sms.courier.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.courier.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.mapper.SceneCaseApiMapper;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.service.impl.SceneCaseApiServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for SceneCaseApiServiceTest")
class SceneCaseApiServiceTest {

    private final SceneCaseApiRepository sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
    private final SceneCaseApiMapper sceneCaseApiMapper = mock(SceneCaseApiMapper.class);
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository =
        mock(CustomizedSceneCaseApiRepository.class);
    private final SceneCaseApiServiceImpl sceneCaseApiService = new SceneCaseApiServiceImpl(sceneCaseApiRepository,
        sceneCaseApiMapper, customizedSceneCaseApiRepository);

    private final static String MOCK_SCENE_CASE_ID = "1";
    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_ORDER_NUMBER = 1;

    @Test
    @DisplayName("Test the batchAdd method in the SceneCaseApi service")
    void batchAdd_test() {
        BatchAddSceneCaseApiRequest addSceneCaseApiDto = getAddDto();
        SceneCaseApiEntity caseApi = SceneCaseApiEntity.builder().build();
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(sceneCaseApiMapper.toSceneCaseApiList(any())).thenReturn(sceneCaseApiList);
        when(sceneCaseApiRepository.insert(any(List.class))).thenReturn(sceneCaseApiList);
        Boolean isSuccess = sceneCaseApiService.batchAdd(addSceneCaseApiDto);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batchAdd method in the SceneCaseApi service throws exception")
    void batchAdd_test_thenThrowException() {
        BatchAddSceneCaseApiRequest addSceneCaseApiDto = getAddDto();
        SceneCaseApiEntity caseApi = SceneCaseApiEntity.builder().build();
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(sceneCaseApiMapper.toSceneCaseApiList(any())).thenReturn(sceneCaseApiList);
        when(sceneCaseApiRepository.insert(any(List.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.batchAdd(addSceneCaseApiDto))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCaseApi service")
    void deleteByIds_test() {
        when(sceneCaseApiRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        Boolean isSuccess = sceneCaseApiService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCaseApi service throws exception")
    void deleteByIds_test_thenThrowException() {
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR)).when(sceneCaseApiRepository)
            .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> sceneCaseApiService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseApi service")
    void edit_test() {
        SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.save(any())).thenReturn(sceneCaseApi);
        Boolean isSuccess = sceneCaseApiService.edit(UpdateSceneCaseApiRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseApi service throws exception")
    void edit_thenThrowException() {
        SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.edit(UpdateSceneCaseApiRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batchEdit method in the SceneCaseApi service")
    void batchEdit_test() {
        BatchUpdateSceneCaseApiRequest dto = getUpdateSortOrder();
        SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder().id(MOCK_ID).build();
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any())).thenReturn(sceneCaseApiList);
        Boolean isSuccess = sceneCaseApiService.batchEdit(dto);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batchEdit method in the SceneCaseApi service throws exception")
    void batchEdit_thenThrowException() {
        BatchUpdateSceneCaseApiRequest dto = getUpdateSortOrder();
        when(sceneCaseApiRepository.saveAll(any()))
            .thenThrow(new ApiTestPlatformException(BATCH_EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.batchEdit(dto)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list method in the SceneCaseApi service")
    void listBySceneCaseId_test() {
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(SceneCaseApiEntity.builder().build());
        when(sceneCaseApiRepository.findAll(any(), any(Sort.class))).thenReturn(sceneCaseApiList);
        SceneCaseApiResponse dto = SceneCaseApiResponse.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiDto(any())).thenReturn(dto);
        List<SceneCaseApiResponse> dtoList = sceneCaseApiService
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
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(SceneCaseApiEntity.builder().build());
        when(sceneCaseApiRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(sceneCaseApiList);
        List<SceneCaseApiEntity> dto = sceneCaseApiService.listBySceneCaseId(MOCK_SCENE_CASE_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list by sceneCaseId method in the SceneCaseApi service")
    void listBySceneCaseId_test_thenThrowException() {
        when(sceneCaseApiRepository.findAll(any(Example.class), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.listBySceneCaseId(MOCK_SCENE_CASE_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getApiBySceneCaseId method in the SceneCaseApi service")
    void getApiBySceneCaseId_test() {
        List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList(SceneCaseApiEntity.builder().build());
        when(sceneCaseApiRepository.findAll(any(), any(Sort.class))).thenReturn(sceneCaseApiList);
        List<SceneCaseApiEntity> dtoList = sceneCaseApiService
            .getApiBySceneCaseId(MOCK_SCENE_CASE_ID, Boolean.FALSE);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getApiBySceneCaseId method in the SceneCaseApi service throws exception")
    void getApiBySceneCaseId_thenThrowException() {
        when(sceneCaseApiRepository.findAll(any(), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.getApiBySceneCaseId(MOCK_SCENE_CASE_ID, Boolean.FALSE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getSceneCaseApiById method in the SceneCaseApi service")
    void getSceneCaseApiById_test() {
        SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder().id(MOCK_ID).build();
        Optional<SceneCaseApiEntity> sceneCaseApiOptional = Optional.ofNullable(sceneCaseApi);
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApiOptional);
        SceneCaseApiResponse sceneCaseApiDto = SceneCaseApiResponse.builder().build();
        when(sceneCaseApiMapper.toSceneCaseApiDto(any())).thenReturn(sceneCaseApiDto);
        SceneCaseApiResponse dto = sceneCaseApiService.getSceneCaseApiById(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getSceneCaseApiById method in the SceneCaseApi service throws exception")
    void getSceneCaseApiById_testThrowException() {
        when(sceneCaseApiRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.getSceneCaseApiById(MOCK_ID));
    }

    @Test
    @DisplayName("Test the updateStatusByApiIds method in the SceneCaseApi service")
    void updateStatusByApiIds_test() {
        List<SceneCaseApiEntity> sceneCaseApiList =
            Lists.newArrayList(SceneCaseApiEntity.builder().apiTestCase(ApiTestCaseEntity.builder().build()).build());
        when(customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(any())).thenReturn(sceneCaseApiList);
        when(sceneCaseApiRepository.saveAll(any())).thenReturn(sceneCaseApiList);
        Boolean isSuccess = sceneCaseApiService.updateStatusByApiIds(Lists.newArrayList(MOCK_SCENE_CASE_ID),
            ApiBindingStatus.BINDING);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the updateStatusByApiIds method in the SceneCaseApi service throws exception")
    void updateStatusByApiIds_testThrowException() {
        when(customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(any()))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> sceneCaseApiService.updateStatusByApiIds(Lists.newArrayList(MOCK_SCENE_CASE_ID),
            ApiBindingStatus.BINDING));
    }

    private BatchUpdateSceneCaseApiRequest getUpdateSortOrder() {
        return BatchUpdateSceneCaseApiRequest.builder()
            .sceneCaseApiRequestList(Lists.newArrayList(UpdateSceneCaseApiRequest.builder()
                .sceneCaseId(MOCK_SCENE_CASE_ID)
                .order(MOCK_ORDER_NUMBER)
                .build())).build();
    }

    private BatchAddSceneCaseApiRequest getAddDto() {
        return BatchAddSceneCaseApiRequest.builder()
            .addSceneCaseApiRequestList(Lists.newArrayList(AddSceneCaseApiRequest.builder().build()))
            .build();
    }

}
