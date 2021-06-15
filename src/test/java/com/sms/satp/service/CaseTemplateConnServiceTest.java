package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import com.sms.satp.service.impl.CaseTemplateConnServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_CONN_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateConnServiceTest")
class CaseTemplateConnServiceTest {

    private CaseTemplateConnRepository caseTemplateConnRepository;
    private CaseTemplateConnMapper caseTemplateConnMapper;
    private CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private CaseTemplateApiService caseTemplateApiService;
    private CaseTemplateConnServiceImpl caseTemplateConnService;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_NUM = 1;

    @BeforeEach
    void setUpBean() {
        caseTemplateConnRepository = mock(CaseTemplateConnRepository.class);
        caseTemplateConnMapper = mock(CaseTemplateConnMapper.class);
        customizedSceneCaseApiRepository = mock(CustomizedSceneCaseApiRepository.class);
        caseTemplateApiService = mock(CaseTemplateApiService.class);
        caseTemplateConnService = new CaseTemplateConnServiceImpl(caseTemplateConnRepository, caseTemplateConnMapper,
            customizedSceneCaseApiRepository, caseTemplateApiService);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateConn service")
    void deleteById_test() {
        Optional<CaseTemplateConn> optional = Optional.ofNullable(CaseTemplateConn.builder().build());
        when(caseTemplateConnRepository.findById(any())).thenReturn(optional);
        doNothing().when(caseTemplateConnRepository).deleteById(any());
        Boolean isSuccess = caseTemplateConnService.deleteById(MOCK_ID);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateConn service thrown exception")
    void deleteById_test_thrownException() {
        when(caseTemplateConnRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_CONN_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.deleteById(MOCK_ID));
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateConn service")
    void deleteByIds_test() {
        when(caseTemplateConnRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        Boolean isSuccess = caseTemplateConnService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateConn service thrown exception")
    void deleteByIds_test_thrownException() {
        when(caseTemplateConnRepository.deleteAllByIdIsIn(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_CONN_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.deleteByIds(Lists.newArrayList(MOCK_ID)));
    }


    @Test
    @DisplayName("Test the listBySceneCaseId method in the CaseTemplateConn service")
    void listBySceneCaseId_test() {
        List<CaseTemplateConn> caseTemplateConnList = Lists.newArrayList(CaseTemplateConn.builder().build());
        when(caseTemplateConnRepository.findAll(any(Example.class))).thenReturn(caseTemplateConnList);
        List<CaseTemplateConn> dto = caseTemplateConnService.listBySceneCaseId(MOCK_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the listBySceneCaseId method in the CaseTemplateConn service thrown exception")
    void listBySceneCaseId_test_thrownException() {
        when(caseTemplateConnRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.listBySceneCaseId(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the listByCassTemplateId method in the CaseTemplateConn service")
    void listByCassTemplateId_test() {
        List<CaseTemplateConn> caseTemplateConnList = Lists.newArrayList(CaseTemplateConn.builder().build());
        when(caseTemplateConnRepository.findAll(any(Example.class))).thenReturn(caseTemplateConnList);
        List<CaseTemplateConn> dto = caseTemplateConnService.listByCassTemplateId(MOCK_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the listByCassTemplateId method in the CaseTemplateConn service thrown exception")
    void listByCassTemplateId_test_thrownException() {
        when(caseTemplateConnRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.listByCassTemplateId(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the listBySceneCaseIdIsRemove method in the CaseTemplateConn service")
    void listBySceneCaseIdIsRemove_test() {
        List<CaseTemplateConn> caseTemplateConnList = Lists.newArrayList(CaseTemplateConn.builder().build());
        when(caseTemplateConnRepository.findAll(any(Example.class))).thenReturn(caseTemplateConnList);
        List<CaseTemplateConn> dto = caseTemplateConnService.listBySceneCaseId(MOCK_ID, Boolean.TRUE);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the listBySceneCaseIdIsRemove method in the CaseTemplateConn service thrown exception")
    void listBySceneCaseIdIsRemove_test_thrownException() {
        when(caseTemplateConnRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.listBySceneCaseId(MOCK_ID, Boolean.TRUE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateConn service")
    void edit_test() {
        CaseTemplateConn conn = CaseTemplateConn.builder().id(MOCK_ID).build();
        when(caseTemplateConnRepository.save(any())).thenReturn(conn);
        Boolean isSuccess = caseTemplateConnService.edit(conn);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateConn service thrown exception")
    void edit_test_thrownException() {
        CaseTemplateConn conn = CaseTemplateConn.builder().id(MOCK_ID).build();
        when(caseTemplateConnRepository.save(any()))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.edit(conn)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editList method in the CaseTemplateConn service")
    void editList_test() {
        List<CaseTemplateConn> caseTemplateConn = Lists.newArrayList(CaseTemplateConn.builder().id(MOCK_ID).build());
        when(caseTemplateConnRepository.saveAll(any())).thenReturn(caseTemplateConn);
        Boolean isSuccess = caseTemplateConnService.editList(caseTemplateConn);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editList method in the CaseTemplateConn service thrown exception")
    void editList_test_thrownException() {
        List<CaseTemplateConn> caseTemplateConn = Lists.newArrayList(CaseTemplateConn.builder().id(MOCK_ID).build());
        when(caseTemplateConnRepository.saveAll(any()))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_CONN_LIST_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.editList(caseTemplateConn))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplateConn service")
    void add_test() {
        AddCaseTemplateConnRequest request = AddCaseTemplateConnRequest.builder().build();
        CaseTemplateConn caseTemplateConn = CaseTemplateConn.builder().id(MOCK_ID).build();
        when(caseTemplateConnMapper.toCaseTemplateConn(any())).thenReturn(caseTemplateConn);
        when(caseTemplateConnRepository.insert(any(CaseTemplateConn.class))).thenReturn(caseTemplateConn);
        Boolean isSuccess = caseTemplateConnService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplateConn service thrown exception")
    void add_test_thrownException() {
        AddCaseTemplateConnRequest request = AddCaseTemplateConnRequest.builder().build();
        CaseTemplateConn caseTemplateConn = CaseTemplateConn.builder().id(MOCK_ID).build();
        when(caseTemplateConnMapper.toCaseTemplateConn(any())).thenReturn(caseTemplateConn);
        when(caseTemplateConnRepository.insert(any(CaseTemplateConn.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_CONN_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.add(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addByIds method in the CaseTemplateConn service")
    void addByIds_test() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiService.getApiByCaseTemplateId(any(), any(Boolean.class))).thenReturn(caseTemplateApiList);
        SceneCaseApi sceneCaseApi = SceneCaseApi.builder().id(MOCK_ID).order(MOCK_NUM).build();
        when(customizedSceneCaseApiRepository.findMaxOrderBySceneCaseId(any())).thenReturn(sceneCaseApi);
        CaseTemplateConn conn = CaseTemplateConn.builder().id(MOCK_ID).build();
        when(caseTemplateConnRepository.insert(any(CaseTemplateConn.class))).thenReturn(conn);
        CaseTemplateConn dto = caseTemplateConnService.addByIds(MOCK_ID, MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the addByIds method in the CaseTemplateConn service thrown exception")
    void addByIds_test_thrownException() {
        when(caseTemplateApiService.getApiByCaseTemplateId(any(), any(Boolean.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_CONN_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.addByIds(MOCK_ID, MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
