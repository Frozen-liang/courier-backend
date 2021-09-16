package com.sms.courier.engine.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.engine.EngineId;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import com.sms.courier.engine.task.SuspiciousEngineManagement;
import com.sms.courier.mapper.EngineMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.EngineMemberRepository;
import java.util.Optional;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for EngineMemberManagement")
public class EngineMemberManagementTest {

    private final EngineMemberRepository engineMemberRepository = mock(EngineMemberRepository.class);
    private final SuspiciousEngineManagement suspiciousEngineManagement = mock(SuspiciousEngineManagement.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final EngineMapper engineMapper = mock(EngineMapper.class);
    private final EngineMemberManagement engineMemberManagement =
        new EngineMemberManagementImpl(engineMemberRepository, commonRepository, suspiciousEngineManagement,
            engineMapper);
    private final EngineMemberEntity engineMember = EngineMemberEntity.builder().destination(EngineId.generate()).id(
        ObjectId.get().toString()).status(EngineStatus.RUNNING).build();
    private static final String DESTINATION = EngineId.generate();
    private static final String ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test for bind in EngineMemberManagement")
    public void bind_test() {
        when(engineMemberRepository.save(engineMember)).thenReturn(engineMember);
        EngineRegistrationRequest engineRegistrationRequest = new EngineRegistrationRequest();
        String result = engineMemberManagement.bind(engineRegistrationRequest);
        assertThat(result).isNotBlank();
    }

    @Test
    @DisplayName("Test for getAvailableMember in EngineMemberManagement")
    public void getAvailableMember_test() {
        when(engineMemberRepository.findAllByStatusAndOpenIsTrue(EngineStatus.RUNNING))
            .thenReturn(Stream.of(engineMember));
        String result = engineMemberManagement.getAvailableMember();
        assertThat(result).isEqualTo(engineMember.getDestination());
    }

    @Test
    @DisplayName("Test for getAvailableMember throw Exception in EngineMemberManagement")
    public void getAvailableMember_exception_test() {
        when(engineMemberRepository.findAllByStatusAndOpenIsTrue(EngineStatus.RUNNING)).thenReturn(Stream.empty());
        assertThatThrownBy(engineMemberManagement::getAvailableMember)
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for caseRecord in EngineMemberManagement")
    public void caseRecord_test() {
        String destination = "destination";
        CaseRecordRequest caseRecordRequest =
            CaseRecordRequest.builder().caseCount(1).sceneCaseCount(1).destination(destination).build();
        when(commonRepository.updateField(any(), any(), any())).thenReturn(Boolean.TRUE);
        engineMemberManagement.caseRecord(caseRecordRequest);
        verify(commonRepository, times(1)).updateField(any(), any(), any());
    }

    @Test
    @DisplayName("Test for active in EngineMemberManagement")
    public void active_test() {
        when(engineMemberRepository.findFirstByDestination(anyString())).thenReturn(Optional.of(engineMember));
        engineMemberManagement.active("sessionId", DESTINATION);
        verify(engineMemberRepository, times(1)).save(engineMember);
    }

    @Test
    @DisplayName("Test for active in EngineMemberManagement")
    public void active_by_waitingForReconnection_test() {
        engineMember.setStatus(EngineStatus.WAITING_FOR_RECONNECTION);
        doNothing().when(suspiciousEngineManagement).remove(anyString());
        when(engineMemberRepository.findFirstByDestination(anyString())).thenReturn(Optional.of(engineMember));
        engineMemberManagement.active("sessionId", DESTINATION);
        verify(suspiciousEngineManagement, times(1)).remove(anyString());
        verify(engineMemberRepository, times(1)).save(engineMember);
    }

    @Test
    @DisplayName("Test for unbind in EngineMemberManagement")
    public void unbind_test() {
        when(engineMemberRepository.findFirstBySessionId(anyString())).thenReturn(Optional.of(engineMember));
        engineMemberManagement.unBind("sessionId");
        when(engineMemberRepository.save(engineMember)).thenReturn(engineMember);
        doNothing().when(suspiciousEngineManagement).add(engineMember.getDestination());
        verify(engineMemberRepository, times(1)).save(engineMember);
        verify(suspiciousEngineManagement, times(1)).add(engineMember.getDestination());
    }

    @Test
    @DisplayName("Test for taskCountRecord in EngineMemberManagement")
    public void taskCountRecord_test() {
        when(commonRepository.updateField(any(), any(), any())).thenReturn(true);
        engineMemberManagement.countTaskRecord(DESTINATION, 1);
        verify(commonRepository, times(1)).updateField(any(), any(), any());
    }

    @Test
    @DisplayName("Test for openEngine in EngineMemberManagement")
    public void openEngine_test() {
        when(commonRepository.updateFieldById(any(), any(), any())).thenReturn(Boolean.TRUE);
        Boolean result = engineMemberManagement.openEngine(ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test for closeEngine in EngineMemberManagement")
    public void closeEngine_test() {
        when(commonRepository.updateFieldById(any(), any(), any())).thenReturn(Boolean.TRUE);
        Boolean result = engineMemberManagement.closeEngine(ID);
        assertThat(result).isTrue();
    }

}
