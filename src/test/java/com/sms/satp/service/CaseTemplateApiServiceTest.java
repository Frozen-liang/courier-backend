package com.sms.satp.service;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.dto.AddCaseTemplateApiDto;
import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.dto.UpdateCaseTemplateApiDto;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.service.impl.CaseTemplateApiServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
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

@DisplayName("Test cases for CaseTemplateApiServiceTest")
class CaseTemplateApiServiceTest {

    private CaseTemplateApiRepository caseTemplateApiRepository;
    private CaseTemplateApiMapper caseTemplateApiMapper;
    private CaseTemplateApiServiceImpl caseTemplateApiService;
    private ApplicationEventPublisher applicationEventPublisher;
    private SceneCaseApiLogMapper sceneCaseApiLogMapper;

    private final static String MOCK_SCENE_CASE_ID = "1";
    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_ORDER_NUMBER = 1;

    @BeforeEach
    void setUpBean() {
        caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
        caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        sceneCaseApiLogMapper = mock(SceneCaseApiLogMapper.class);
        caseTemplateApiService = new CaseTemplateApiServiceImpl(caseTemplateApiRepository, caseTemplateApiMapper,
            applicationEventPublisher, sceneCaseApiLogMapper);
    }

    @Test
    @DisplayName("Test the batch method in the CaseTemplateApi service")
    void batch_test() {
        AddCaseTemplateApiDto addCaseTemplateApiDto = getAddDto();
        CaseTemplateApi caseApi = CaseTemplateApi.builder().build();
        List<CaseTemplateApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class))).thenReturn(sceneCaseApiList);
        caseTemplateApiService.batch(addCaseTemplateApiDto);
        verify(caseTemplateApiRepository, times(1)).insert(any(List.class));
    }

    @Test
    @DisplayName("Test the batch method in the CaseTemplateApi service throws exception")
    void batch_test_thenThrowException() {
        AddCaseTemplateApiDto addCaseTemplateApiDto = getAddDto();
        CaseTemplateApi caseApi = CaseTemplateApi.builder().build();
        List<CaseTemplateApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.batch(addCaseTemplateApiDto))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batch method in the CaseTemplateApi service")
    void deleteById_test() {
        Optional<CaseTemplateApi> caseTemplateApi = Optional.ofNullable(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        doNothing().when(caseTemplateApiRepository).deleteById(any());
        caseTemplateApiService.deleteById(MOCK_ID);
        verify(caseTemplateApiRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Test the batch method in the CaseTemplateApi service throws exception")
    void deleteById_test_thenThrowException() {
        Optional<CaseTemplateApi> caseTemplateApi = Optional.ofNullable(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR)).when(caseTemplateApiRepository)
            .deleteById(any());
        assertThatThrownBy(() -> caseTemplateApiService.deleteById(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service")
    void edit_test() {
        CaseTemplateApi sceneCaseApi = CaseTemplateApi.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApi(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenReturn(sceneCaseApi);
        caseTemplateApiService.edit(CaseTemplateApiDto.builder().build());
        verify(caseTemplateApiRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service throws exception")
    void edit_thenThrowException() {
        CaseTemplateApi sceneCaseApi = CaseTemplateApi.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApi(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.edit(CaseTemplateApiDto.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batchEdit method in the CaseTemplateApi service")
    void batchEdit_test() {
        UpdateCaseTemplateApiDto dto = getUpdateSortOrder();
        CaseTemplateApi caseTemplateApi = CaseTemplateApi.builder().id(MOCK_ID).build();
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(caseTemplateApi);
        when(caseTemplateApiRepository.saveAll(any())).thenReturn(caseTemplateApiList);
        caseTemplateApiService.batchEdit(dto);
        verify(caseTemplateApiRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the batchEdit method in the CaseTemplateApi service throws exception")
    void batchEdit_thenThrowException() {
        UpdateCaseTemplateApiDto dto = getUpdateSortOrder();
        when(caseTemplateApiRepository.saveAll(any()))
            .thenThrow(new ApiTestPlatformException(BATCH_EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.batchEdit(dto)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list method in the CaseTemplateApi service")
    void listBySceneCaseId_test() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class))).thenReturn(caseTemplateApiList);
        CaseTemplateApiDto dto = CaseTemplateApiDto.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiDto(any())).thenReturn(dto);
        List<CaseTemplateApiDto> dtoList = caseTemplateApiService
            .listByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the CaseTemplateApi service throws exception")
    void listBySceneCaseId_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenReturnSceneCaseApi() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.findAll(any(Example.class))).thenReturn(caseTemplateApiList);
        List<CaseTemplateApi> dto = caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getCaseTemplateApiById method in the CaseTemplateApi service")
    void getSceneCaseApiById_test() {
        CaseTemplateApi caseTemplateApi = CaseTemplateApi.builder().id(MOCK_ID).build();
        Optional<CaseTemplateApi> caseTemplateApiOptional = Optional.ofNullable(caseTemplateApi);
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApiOptional);
        CaseTemplateApiDto caseTemplateApiDto = CaseTemplateApiDto.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiDto(any())).thenReturn(caseTemplateApiDto);
        CaseTemplateApiDto dto = caseTemplateApiService.getSceneCaseApiById(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getCaseTemplateApiById method in the CaseTemplateApi service throws exception")
    void getSceneCaseApiById_testThrowException() {
        when(caseTemplateApiRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.getSceneCaseApiById(MOCK_ID));
    }

    private UpdateCaseTemplateApiDto getUpdateSortOrder() {
        return UpdateCaseTemplateApiDto.builder()
            .apiDtoList(Lists.newArrayList(CaseTemplateApiDto.builder()
                .caseTemplateId(MOCK_SCENE_CASE_ID)
                .orderNumber(MOCK_ORDER_NUMBER)
                .build())).build();
    }

    private AddCaseTemplateApiDto getAddDto() {
        return AddCaseTemplateApiDto.builder()
            .apiDtoList(Lists.newArrayList(CaseTemplateApiDto.builder().build()))
            .build();
    }

}
