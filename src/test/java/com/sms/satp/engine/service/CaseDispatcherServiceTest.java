package com.sms.satp.engine.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.engine.EngineMemberManagement;
import com.sms.satp.engine.service.impl.CaseDispatcherServiceImpl;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.common.CaseReport;
import java.util.Set;
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
    private final ApiTestCaseJob apiTestCaseJob = ApiTestCaseJob.builder().build();
    private final SceneCaseJob sceneCaseJob = SceneCaseJob.builder().build();
    private static final String USER_ID = ObjectId.get().toString();
    private static final String MESSAGE = "message";
    private final CaseReport caseReport = CaseReport.builder().build();

    @Test
    @DisplayName("Test the dispatch method in the CaseDispatcherService service")
    public void dispatch_test() {
        when(engineMemberManagement.getAvailableMembers()).thenReturn(Set.of("1", "2"));
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        caseDispatcherService.dispatch(apiTestCaseJob);
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    @Test
    @DisplayName("Test the dispatch method in the CaseDispatcherService service")
    public void dispatch2_test() {
        when(engineMemberManagement.getAvailableMembers()).thenReturn(Set.of("1", "2"));
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        caseDispatcherService.dispatch(sceneCaseJob);
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
    @DisplayName("Test the sendMessage method in the CaseDispatcherService service")
    public void sendMessage_test() {
        caseDispatcherService.sendErrorMessage(USER_ID, MESSAGE);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), any(Object.class));
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }
}
