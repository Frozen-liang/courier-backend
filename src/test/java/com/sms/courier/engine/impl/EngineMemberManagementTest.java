package com.sms.courier.engine.impl;

import static com.sms.courier.common.exception.ErrorCode.CREATE_ENGINE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_ENGINE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_ENGINE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineId;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.EngineSettingService;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.mapper.EngineMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for EngineMemberManagement")
public class EngineMemberManagementTest {

    private final EngineMemberRepository engineMemberRepository = mock(EngineMemberRepository.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final EngineMapper engineMapper = mock(EngineMapper.class);
    private final DockerService dockerService = mock(DockerService.class);
    private final EngineSettingService engineSettingService = mock(EngineSettingService.class);
    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final EngineMemberManagement engineMemberManagement =
        new EngineMemberManagementImpl(engineMemberRepository, commonRepository,
            engineMapper, dockerService, engineSettingService, jwtTokenManager);
    private final EngineMemberEntity engineMember = EngineMemberEntity.builder().destination(EngineId.generate()).id(
        ObjectId.get().toString()).status(EngineStatus.RUNNING).build();
    private static final String DESTINATION = EngineId.generate();
    private static final String ID = ObjectId.get().toString();

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

    @Test
    @DisplayName("Test for createEngine in EngineMemberManagement")
    public void createEngine_test() {
        EngineSettingResponse engineSetting = EngineSettingResponse.builder().build();
        when(engineSettingService.findOne()).thenReturn(engineSetting);
        when(engineMemberRepository.count()).thenReturn(2L);
        doNothing().when(dockerService).startContainer(any());
        Boolean result = engineMemberManagement.createEngine();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An custom exception occurred while create engine.")
    public void createEngine_custom_exception_test() {
        EngineSettingResponse engineSetting = EngineSettingResponse.builder().build();
        when(engineSettingService.findOne()).thenReturn(engineSetting);
        when(engineMemberRepository.count()).thenReturn(2L);
        doThrow(ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, engineSetting.getContainerName())).when(dockerService)
            .startContainer(any());
        assertThatThrownBy(engineMemberManagement::createEngine).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @Test
    @DisplayName("An system exception occurred while create engine.")
    public void createEngine_system_exception_test() {
        EngineSettingResponse engineSetting = EngineSettingResponse.builder().build();
        when(engineSettingService.findOne()).thenReturn(engineSetting);
        when(engineMemberRepository.count()).thenReturn(2L);
        doThrow(new RuntimeException()).when(dockerService).startContainer(any());
        assertThatThrownBy(engineMemberManagement::createEngine).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(CREATE_ENGINE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test for restartEngine in EngineMemberManagement")
    public void restartEngine_test() {
        String name = "courier-engine-1";
        doNothing().when(dockerService).restartContainer(name);
        Boolean result = engineMemberManagement.restartEngine(name);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An custom exception occurred while restart engine.")
    public void restartEngine_custom_exception_test() {
        String name = "courier-engine-1";
        doThrow(ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, name)).when(dockerService).restartContainer(name);
        assertThatThrownBy(() -> engineMemberManagement.restartEngine(name))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @Test
    @DisplayName("An system exception occurred while restart engine.")
    public void restartEngine_system_exception_test() {
        String name = "courier-engine-1";
        doThrow(new RuntimeException()).when(dockerService).restartContainer(name);
        assertThatThrownBy(() -> engineMemberManagement.restartEngine(name))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(RESTART_ENGINE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test for deleteEngine in EngineMemberManagement")
    public void deleteEngine_test() {
        String name = "courier-engine-1";
        doNothing().when(dockerService).deleteContainer(name);
        Boolean result = engineMemberManagement.deleteEngine(name);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An custom exception occurred while delete engine.")
    public void deleteEngine_custom_exception_test() {
        String name = "courier-engine-1";
        doThrow(ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, name)).when(dockerService).deleteContainer(name);
        assertThatThrownBy(() -> engineMemberManagement.deleteEngine(name))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(NO_SUCH_CONTAINER_ERROR.getCode());
    }

    @Test
    @DisplayName("An system exception occurred while delete engine.")
    public void deleteEngine_system_exception_test() {
        String name = "courier-engine-1";
        doThrow(new RuntimeException()).when(dockerService).deleteContainer(name);
        assertThatThrownBy(() -> engineMemberManagement.deleteEngine(name))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_ENGINE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test for queryLog in EngineMemberManagement")
    public void queryLog_test() {
        DockerLogRequest request = DockerLogRequest.builder().build();
        doNothing().when(dockerService).queryLog(request);
        Boolean result = engineMemberManagement.queryLog(request);
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("Test for batchDeleteEngine in EngineMemberManagement")
    public void batchDeleteEngine_test() {
        List<String> names = List.of("courier-engine-1");
        doNothing().when(dockerService).deleteContainer(anyString());
        Boolean result = engineMemberManagement.batchDeleteEngine(names);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An custom exception occurred while batch delete engine.")
    public void batchDeleteEngine_custom_exception_test() {
        List<String> names = List.of("courier-engine-1");
        doThrow(ExceptionUtils.mpe(NO_SUCH_CONTAINER_ERROR, "name")).when(dockerService).deleteContainer(anyString());
        Boolean result = engineMemberManagement.batchDeleteEngine(names);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An system exception occurred while batch delete engine.")
    public void batchDeleteEngine_system_exception_test() {
        List<String> names = List.of("courier-engine-1");
        doThrow(new RuntimeException()).when(dockerService).deleteContainer(anyString());
        Boolean result = engineMemberManagement.batchDeleteEngine(names);
        assertThat(result).isTrue();
    }
}
