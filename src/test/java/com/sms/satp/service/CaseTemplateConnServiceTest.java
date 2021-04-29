package com.sms.satp.service;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.service.impl.CaseTemplateConnServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import static com.sms.satp.common.ErrorCode.DELETE_CASE_TEMPLATE_CONN_ERROR;
import static com.sms.satp.common.ErrorCode.GET_CASE_TEMPLATE_CONN_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test cases for CaseTemplateConnServiceTest")
public class CaseTemplateConnServiceTest {

    private CaseTemplateConnRepository caseTemplateConnRepository;
    private CaseTemplateConnServiceImpl caseTemplateConnService;

    private final static String MOCK_ID = new ObjectId().toString();

    @BeforeEach
    void setUpBean() {
        caseTemplateConnRepository = mock(CaseTemplateConnRepository.class);
        caseTemplateConnService = new CaseTemplateConnServiceImpl(caseTemplateConnRepository);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateConn service")
    void deleteById_test() {
        Optional<CaseTemplateConn> optional = Optional.ofNullable(CaseTemplateConn.builder().build());
        when(caseTemplateConnRepository.findById(any())).thenReturn(optional);
        doNothing().when(caseTemplateConnRepository).deleteById(any());
        caseTemplateConnService.deleteById(MOCK_ID);
        verify(caseTemplateConnRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplateConn service thrown exception")
    void deleteById_test_thrownException() {
        when(caseTemplateConnRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_CONN_ERROR));
        assertThatThrownBy(() -> caseTemplateConnService.deleteById(MOCK_ID));
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

}
