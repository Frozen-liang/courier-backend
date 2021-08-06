package com.sms.courier.engine.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.service.impl.CaseDispatcherServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@DisplayName("Test for CaseDispatcherService")
public class CaseDispatcherServiceTest {


    private final EngineMemberManagement engineMemberManagement = mock(EngineMemberManagement.class);
    private final SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
    private final CaseDispatcherService caseDispatcherService = new CaseDispatcherServiceImpl(engineMemberManagement,
        simpMessagingTemplate);
    private final ApiTestCaseJobResponse apiTestCaseJobResponse = ApiTestCaseJobResponse.builder().build();
    private final SceneCaseJobResponse sceneCaseJob = SceneCaseJobResponse.builder().build();
    private static final String USER_ID = ObjectId.get().toString();
    private static final String MESSAGE = "message";
    private static final String DESTINATION = "engine/123/invoke";
    private final ApiTestCaseJobReportResponse caseReport = ApiTestCaseJobReportResponse.builder().build();
    private final SceneCaseJobReportResponse sceneCaseJobReportResponse = SceneCaseJobReportResponse.builder().build();

    @Test
    @DisplayName("Test the dispatch method in the CaseDispatcherService service")
    public void dispatch_test() {
        when(engineMemberManagement.getAvailableMember()).thenReturn(DESTINATION);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        caseDispatcherService.dispatch(apiTestCaseJobResponse);
        doNothing().when(engineMemberManagement).countTaskRecord(DESTINATION, 1);
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Test the dispatch method in the CaseDispatcherService service")
    public void dispatch2_test() {
        when(engineMemberManagement.getAvailableMember()).thenReturn(DESTINATION);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        caseDispatcherService.dispatch(sceneCaseJob);
        doNothing().when(engineMemberManagement).countTaskRecord(DESTINATION, 1);
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

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
    @DisplayName("Test the sendMessage method in the CaseDispatcherService service")
    public void sendMessage_test() {
        caseDispatcherService.sendErrorMessage(USER_ID, MESSAGE);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }
}
