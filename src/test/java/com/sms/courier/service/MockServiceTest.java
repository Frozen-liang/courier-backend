package com.sms.courier.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertFalse;
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.mapper.MockSettingMapper;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.service.impl.MockServiceImpl;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test cases for MockService")
public class MockServiceTest {

    private final DockerService dockerService = mock(DockerService.class);
    private final MockSettingRepository mockSettingRepository = mock(MockSettingRepository.class);
    private final MockSettingMapper mockSettingMapper = mock(MockSettingMapper.class);
    private final MockService mockService = new MockServiceImpl(dockerService, mockSettingRepository,
        mockSettingMapper);
    private final static String ID = new ObjectId().toString();

    @Test
    public void createMock_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).startContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.createMock();
        assertTrue(isSuccess);
    }

    @Test
    public void createMockStatusEquals_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().containerStatus(ContainerStatus.DESTROY)
            .id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).startContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.createMock();
        assertTrue(isSuccess);
    }

    @Test
    public void createMockStatusNotEquals_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().containerStatus(ContainerStatus.START)
            .id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).startContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.createMock();
        assertFalse(isSuccess);
    }

    @Test
    public void createMock_test_thenSmsException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new ApiTestPlatformException(ErrorCode.CREATE_MOCK_ERROR)).when(dockerService).startContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::createMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    public void createMock_test_thenException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new RuntimeException()).when(dockerService).startContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::createMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    public void restartMockStatusEquals_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().containerStatus(ContainerStatus.DIE)
            .id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).restartContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.restartMock();
        assertTrue(isSuccess);
    }

    @Test
    public void restartMockStatusNotEquals_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().containerStatus(ContainerStatus.DESTROY)
            .id(ID).build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).restartContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.restartMock();
        assertFalse(isSuccess);
    }

    @Test
    public void restart_test_thenSmsException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).containerStatus(ContainerStatus.DIE)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new ApiTestPlatformException(ErrorCode.CREATE_MOCK_ERROR)).when(dockerService).restartContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::restartMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    public void restart_test_thenException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).containerStatus(ContainerStatus.DIE)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new RuntimeException()).when(dockerService).restartContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::restartMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    public void deleteMock_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).containerStatus(ContainerStatus.DIE)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).deleteContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.deleteMock();
        assertTrue(isSuccess);
    }

    @Test
    public void deleteMockReturnFalse_test() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID)
            .containerStatus(ContainerStatus.DESTROY)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doNothing().when(dockerService).deleteContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        Boolean isSuccess = mockService.deleteMock();
        assertFalse(isSuccess);
    }

    @Test
    public void deleteMock_test_thenSmsException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).containerStatus(ContainerStatus.DIE)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new ApiTestPlatformException(ErrorCode.DELETE_MOCK_ERROR)).when(dockerService).deleteContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::deleteMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    public void deleteMock_test_thenException() {
        MockSettingEntity mockSettingEntity = MockSettingEntity.builder().id(ID).containerStatus(ContainerStatus.DIE)
            .build();
        Optional<MockSettingEntity> optional = Optional.ofNullable(mockSettingEntity);
        when(mockSettingRepository.findOne(any())).thenReturn(optional);
        doThrow(new RuntimeException()).when(dockerService).deleteContainer(any());
        when(mockSettingRepository.save(any())).thenReturn(mockSettingEntity);
        assertThatThrownBy(mockService::deleteMock).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for queryLog in MockService")
    public void queryLog_test() {
        DockerLogRequest request = DockerLogRequest.builder().build();
        doNothing().when(dockerService).queryLog(request);
        Boolean result = mockService.queryLog(request);
        assertThat(result).isTrue();
    }
}
