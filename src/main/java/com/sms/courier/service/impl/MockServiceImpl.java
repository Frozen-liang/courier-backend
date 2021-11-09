package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.MOCK_SETTING;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.exception.ErrorCode.CREATE_MOCK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_MOCK_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_MOCK_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.entity.mock.MockSettingEntity;
import com.sms.courier.mapper.MockSettingMapper;
import com.sms.courier.repository.MockSettingRepository;
import com.sms.courier.service.MockService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockServiceImpl implements MockService {

    private final DockerService dockerService;
    private final MockSettingRepository mockSettingRepository;
    private final MockSettingMapper mockSettingMapper;

    public MockServiceImpl(DockerService dockerService, MockSettingRepository mockSettingRepository,
        MockSettingMapper mockSettingMapper) {
        this.dockerService = dockerService;
        this.mockSettingRepository = mockSettingRepository;
        this.mockSettingMapper = mockSettingMapper;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = MOCK_SETTING)
    public Boolean createMock() {
        try {
            Example<MockSettingEntity> example = Example.of(MockSettingEntity.builder().removed(Boolean.FALSE).build(),
                ExampleMatcher.matching());
            MockSettingEntity mockSettingEntity = mockSettingRepository.findOne(example)
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_MOCK_SETTING_BY_ID_ERROR));
            if (Objects.isNull(mockSettingEntity.getContainerStatus())
                || Objects.equals(mockSettingEntity.getContainerStatus(), ContainerStatus.DESTROY)) {
                dockerService.startContainer(mockSettingMapper.toContainerSetting(mockSettingEntity));
                mockSettingRepository.save(mockSettingEntity);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Create mock error!", e);
            throw ExceptionUtils.mpe(CREATE_MOCK_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = MOCK_SETTING)
    public Boolean restartMock() {
        try {
            Example<MockSettingEntity> example = Example.of(MockSettingEntity.builder().removed(Boolean.FALSE).build(),
                ExampleMatcher.matching());
            MockSettingEntity mockSettingEntity = mockSettingRepository.findOne(example)
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_MOCK_SETTING_BY_ID_ERROR));

            if (Objects.equals(mockSettingEntity.getContainerStatus(), ContainerStatus.DESTROY)) {
                return Boolean.FALSE;
            }
            dockerService.restartContainer(mockSettingEntity.getContainerName());
            mockSettingRepository.save(mockSettingEntity);
            return Boolean.TRUE;

        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Restart mock error!", e);
            throw ExceptionUtils.mpe(RESTART_MOCK_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = MOCK_SETTING)
    public Boolean deleteMock() {
        try {
            Example<MockSettingEntity> example = Example.of(MockSettingEntity.builder().removed(Boolean.FALSE).build(),
                ExampleMatcher.matching());
            MockSettingEntity mockSettingEntity = mockSettingRepository.findOne(example)
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_MOCK_SETTING_BY_ID_ERROR));
            if (Objects.equals(mockSettingEntity.getContainerStatus(), ContainerStatus.DESTROY)) {
                return Boolean.FALSE;
            }
            dockerService.deleteContainer(mockSettingEntity.getContainerName());
            mockSettingRepository.save(mockSettingEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Delete mock error!", e);
            throw ExceptionUtils.mpe(DELETE_MOCK_ERROR);
        }
    }

}
