package com.sms.courier.engine.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.engine.service.impl.CaseDispatcherServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@DisplayName("Test for CaseDispatcherService")
public class CaseDispatcherServiceTest {


    private final SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
    private final CaseDispatcherService caseDispatcherService = new CaseDispatcherServiceImpl(simpMessagingTemplate);
    private static final String USER_ID = ObjectId.get().toString();
    private static final String MESSAGE = "message";
    private final ApiTestCaseJobReportResponse caseReport = ApiTestCaseJobReportResponse.builder().build();
    private final SceneCaseJobReportResponse sceneCaseJobReportResponse = SceneCaseJobReportResponse.builder().build();

    @Test
    @DisplayName("Test the sendJobReport method in the CaseDispatcherService service")
    public void sendJobReport_test() {
        caseDispatcherService.sendJobReport(USER_ID, caseReport);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Test the sendJobReport method in the CaseDispatcherService service")
    public void sendSceneJobReport_test() {
        caseDispatcherService.sendJobReport(USER_ID, sceneCaseJobReportResponse);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Test the sendCaseMessage method in the CaseDispatcherService service")
    public void sendCaseMessage_test() {
        caseDispatcherService.sendCaseErrorMessage(USER_ID, MESSAGE);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Test the sendSceneCaseErrorMessage method in the CaseDispatcherService service")
    public void sendSceneCaseMessage_test() {
        caseDispatcherService.sendSceneCaseErrorMessage(USER_ID, MESSAGE);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }
}
