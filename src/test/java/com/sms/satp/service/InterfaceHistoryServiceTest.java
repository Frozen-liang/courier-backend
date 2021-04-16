package com.sms.satp.service;

import static com.sms.satp.common.ErrorCode.GET_INTERFACE_HISTORY_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_INTERFACE_HISTORY_LIST_ERROR;
import static com.sms.satp.common.ErrorCode.SAVE_INTERFACE_HISTORY_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.ApplicationTests;
import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceHistory;
import com.sms.satp.entity.InterfaceShowInHistory;
import com.sms.satp.mapper.InterfaceHistoryMapper;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.repository.InterfaceHistoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest(classes = ApplicationTests.class)
@DisplayName("Test cases for InterfaceHistoryService")
public class InterfaceHistoryServiceTest {

    @MockBean
    InterfaceHistoryRepository interfaceHistoryRepository;

    @SpyBean
    InterfaceHistoryService interfaceHistoryService;

    @SpyBean
    InterfaceHistoryMapper interfaceHistoryMapper;

    private final static String ID = "25";
    private final static String PROJECT_ID = "25";
    private final static String TITLE = "title";
    private final static String METHOD = "get";
    private final static String PATH = "/test";

    @Test
    @DisplayName("Test the saveAsHistory method in the InterfaceHistory service")
    void saveAsHistoryTest() {
        when(interfaceHistoryRepository.save(any(InterfaceHistory.class))).thenReturn(null);
        ApiInterface apiInterface = ApiInterface.builder().build();
        interfaceHistoryService.saveAsHistory(apiInterface);
        verify(interfaceHistoryRepository, times(1)).save(any(InterfaceHistory.class));
    }

    @Test
    @DisplayName("Test the getHistoryById method in the InterfaceHistory service")
    void getHistoryByIdTest() {
        InterfaceHistory interfaceHistory = InterfaceHistory.builder().title(TITLE).build();
        Optional<InterfaceHistory> interfaceHistoryOptional = Optional.ofNullable(interfaceHistory);
        when(interfaceHistoryRepository.findById(anyString())).thenReturn(interfaceHistoryOptional);
        InterfaceHistory result = interfaceHistoryService.getHistoryById(ID);
        assertThat(result.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("Test the getHistoryList method in the InterfaceHistory service")
    void getHistoryListTest() {
        List<InterfaceShowInHistory> interfaceShowInHistories = new ArrayList<>();
        when(interfaceHistoryRepository.findByProjectIdAndMethodAndPath(PROJECT_ID, RequestMethod.GET, PATH)).thenReturn(interfaceShowInHistories);
        List<InterfaceShowInHistory> resultList = interfaceHistoryService.getHistoryList(PROJECT_ID, METHOD, PATH);
        assertThat(resultList).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while saving InterfaceHistory")
    void saveAsHistory_exception_test() {
        doThrow(new RuntimeException()).when(interfaceHistoryRepository).save(any(InterfaceHistory.class));
        assertThatThrownBy(() -> interfaceHistoryService.saveAsHistory(ApiInterface.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(SAVE_INTERFACE_HISTORY_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting the InterfaceHistory")
    void getHistoryById_exception_test() {
        doThrow(new RuntimeException()).when(interfaceHistoryRepository).findById(anyString());
        assertThatThrownBy(() -> interfaceHistoryService.getHistoryById(ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_INTERFACE_HISTORY_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while getting the InterfaceHistory List")
    void getHistoryList_exception_test() {
        doThrow(new RuntimeException()).when(interfaceHistoryRepository).findByProjectIdAndMethodAndPath(PROJECT_ID, RequestMethod.GET, PATH);
        assertThatThrownBy(() -> interfaceHistoryService.getHistoryList(PROJECT_ID, METHOD, PATH))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_INTERFACE_HISTORY_LIST_ERROR.getCode());
    }

}