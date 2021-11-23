package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.UPDATE_CASE_BY_API_ERROR;
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
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.PageDto;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCasePageRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.request.UpdateCaseByApiRequest;
import com.sms.courier.dto.request.UpdateCaseByApiRequest.CaseRequest;
import com.sms.courier.dto.response.ApiTestCasePageResponse;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CustomizedApiTestCaseJobRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.impl.ApiTestCaseServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

@DisplayName("Tests for ApiTestCaseService")
class ApiTestCaseServiceTest {

    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository = mock(
        CustomizedApiTestCaseRepository.class);
    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository = mock(
        CustomizedApiTestCaseJobRepository.class);
    private final CaseApiCountHandler caseApiCountHandler = mock(CaseApiCountHandler.class);
    private final ApiTestCaseService apiTestCaseService = new ApiTestCaseServiceImpl(
        apiTestCaseRepository, customizedApiTestCaseRepository, apiTestCaseMapper,
        caseApiCountHandler);
    private final ApiTestCaseEntity apiTestCase =
        ApiTestCaseEntity.builder().id(ID).apiEntity(ApiEntity.builder().id(ID).build()).build();
    private final ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
        .id(ID).build();
    private final ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
        .id(ID).apiEntity(ApiRequest.builder().id(ID).build()).build();
    private static final boolean REMOVED = Boolean.FALSE;
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final ObjectId API_ID = ObjectId.get();
    private static final ObjectId PROJECT_ID = ObjectId.get();

    @Test
    @DisplayName("Test the findById method in the ApiTestCase service")
    public void findById_test() {
        when(apiTestCaseRepository.findById(ID)).thenReturn(Optional.of(apiTestCase));
        when(apiTestCaseMapper.toDto(apiTestCase)).thenReturn(apiTestCaseResponse);
        ApiTestCaseResponse result1 = apiTestCaseService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("Test the findOne method in the ApiTestCase service")
    public void findOne_test() {
        when(apiTestCaseRepository.findById(ID)).thenReturn(Optional.of(apiTestCase));
        ApiTestCaseEntity result = apiTestCaseService.findOne(ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCase")
    public void findById_exception_test() {
        when(apiTestCaseRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiTestCaseService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiTestCase service")
    public void add_test() {
        CustomUser customUser = mock(CustomUser.class);
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.insert(any(ApiTestCaseEntity.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.add(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTestCase")
    public void add_exception_test() {
        when(apiTestCaseMapper.toEntity(any())).thenReturn(ApiTestCaseEntity.builder().build());
        doThrow(new RuntimeException()).when(apiTestCaseRepository).insert(any(ApiTestCaseEntity.class));
        assertThatThrownBy(() -> apiTestCaseService.add(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTestCase service")
    public void edit_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiTestCaseRepository.save(any(ApiTestCaseEntity.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.edit(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTestCase")
    public void edit_exception_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiTestCaseRepository).save(any(ApiTestCaseEntity.class));
        assertThatThrownBy(() -> apiTestCaseService.edit(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ApiTestCase")
    public void edit_not_exist_exception_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> apiTestCaseService.edit(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTestCase service")
    public void list_test() {
        ArrayList<ApiTestCaseResponse> apiTestCaseResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTestCaseResponseList.add(ApiTestCaseResponse.builder().build());
        }
        when(customizedApiTestCaseRepository.listByJoin(any(), any(), anyBoolean()))
            .thenReturn(apiTestCaseResponseList);
        List<ApiTestCaseResponse> result = apiTestCaseService.list(API_ID, PROJECT_ID, REMOVED);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCase list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(customizedApiTestCaseRepository).listByJoin(any(), any(), anyBoolean());
        assertThatThrownBy(() -> apiTestCaseService.list(API_ID, PROJECT_ID, REMOVED))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTestCase service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(customizedApiTestCaseRepository.deleteByIds(ids)).thenReturn(Boolean.TRUE);
        assertThat(apiTestCaseService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTestCase")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(customizedApiTestCaseRepository)
            .deleteByIds(ids);
        assertThatThrownBy(() -> apiTestCaseService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the updateApiTestCaseStatusByApiId method in the ApiTestCase service")
    public void updateApiTestCaseStatusByApiId_test() {
        doNothing().when(customizedApiTestCaseRepository).updateApiTestCaseStatusByApiId(any(), any());
        apiTestCaseService.updateApiTestCaseStatusByApiId(Collections.singletonList(ObjectId.get().toString()),
            ApiBindingStatus.BINDING);
        verify(customizedApiTestCaseRepository, times(1)).updateApiTestCaseStatusByApiId(any(), any());
    }

    @Test
    @DisplayName("Test the deleteByIds method in the ApiTestCase service")
    public void deleteByIds_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(apiTestCaseRepository).deleteAllByIdIn(ids);
        assertThat(apiTestCaseService.deleteByIds(ids)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteAll method in the ApiTestCase service")
    public void deleteAll_test() {
        doNothing().when(apiTestCaseRepository).deleteAllByRemovedIsTrue();
        assertThat(apiTestCaseService.deleteAll()).isTrue();
    }

    @Test
    @DisplayName("Test the recover method in the ApiTestCase service")
    public void recover_test() {
        when(customizedApiTestCaseRepository.recover(any())).thenReturn(Boolean.TRUE);
        List<String> apiIds = Lists.newArrayList(ID);
        when(customizedApiTestCaseRepository.findApiIdsByTestIds(any())).thenReturn(apiIds);
        Boolean isSuccess = apiTestCaseService.recover(Lists.newArrayList(ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the count method in the ApiTestCase service")
    public void count_test() {
        when(apiTestCaseRepository.count(any())).thenReturn(1L);
        Long count = apiTestCaseService.count(ID);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test the insertTestResult method in the ApiTestCase service")
    public void insertTestResult_test() {
        when(apiTestCaseRepository.findById(any())).thenReturn(Optional.of(ApiTestCaseEntity.builder().build()));
        apiTestCaseService.insertTestResult(ID, TestResult.builder().build());
        verify(apiTestCaseRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test the countByProjectIds method in the ApiTestCase service")
    public void countByProjectIds_test() {
        when(customizedApiTestCaseRepository.countByProjectIds(any(), any())).thenReturn(1L);
        Long count = apiTestCaseService.countByProjectIds(Lists.newArrayList(ID), LocalDateTime.now());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Test the countByProjectIds method in the ApiTestCase service")
    public void countByProjectIds_projectIdIsNull_test() {
        Long count = apiTestCaseService.countByProjectIds(Lists.newArrayList(), LocalDateTime.now());
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @DisplayName("Test the getCasePageByProjectIdsAndCreateDate method in the ApiTestCase service")
    public void getCasePageByProjectIdsAndCreateDate_test() {
        Page<ApiTestCaseResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiTestCaseResponse.builder().build()));
        when(customizedApiTestCaseRepository.getCasePageByProjectIdsAndCreateDate(any(), any(), any()))
            .thenReturn(page);
        Page<ApiTestCaseResponse> pageDto =
            apiTestCaseService.getCasePageByProjectIdsAndCreateDate(Lists.newArrayList(ID), LocalDateTime.now(),
                new PageDto());
        assertThat(pageDto.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test the page method in the ApiTestCase service")
    public void page_test() {
        when(customizedApiTestCaseRepository.page(any())).thenReturn(Page.empty());
        Page<ApiTestCasePageResponse> page = apiTestCaseService.page(new ApiTestCasePageRequest());
        assertThat(page).isEmpty();
    }

    @Test
    @DisplayName("Test the updateCaseByApi method in the ApiTestCase service")
    public void updateCaseByApi_test() {
        List<UpdateCaseByApiRequest> requests = getUpdateCaseByApiRequests();
        when(apiTestCaseRepository.findByIdIn(any(Set.class))).thenReturn(List.of(ApiTestCaseEntity.builder().build()));
        Boolean result = apiTestCaseService.updateCaseByApi(requests);
        assert result;
    }

    @Test
    @DisplayName("An exception occurred while update case by api")
    public void updateCaseByApi_exception_test() {
        List<UpdateCaseByApiRequest> requests = getUpdateCaseByApiRequests();
        when(apiTestCaseRepository.findByIdIn(any(Set.class))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> apiTestCaseService.updateCaseByApi(requests))
            .isInstanceOf(ApiTestPlatformException.class).extracting("code")
            .isEqualTo(UPDATE_CASE_BY_API_ERROR.getCode());
    }


    private List<UpdateCaseByApiRequest> getUpdateCaseByApiRequests() {
        UpdateCaseByApiRequest updateCaseByApiRequest = new UpdateCaseByApiRequest();
        updateCaseByApiRequest.setApi(ApiRequest.builder().build());
        updateCaseByApiRequest.setCaseList(List.of(new CaseRequest()));
        List<UpdateCaseByApiRequest> requests = List.of(updateCaseByApiRequest);
        return requests;
    }

}