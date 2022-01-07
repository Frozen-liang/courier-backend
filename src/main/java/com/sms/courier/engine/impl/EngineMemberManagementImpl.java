package com.sms.courier.engine.impl;

import static com.sms.courier.common.enums.ContainerStatus.DESTROY;
import static com.sms.courier.common.enums.OperationModule.ENGINE_MEMBER;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.RESTART;
import static com.sms.courier.common.exception.ErrorCode.CREATE_ENGINE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_ENGINE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.NO_SUCH_CONTAINER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RESTART_ENGINE_ERROR;
import static com.sms.courier.common.field.EngineMemberField.NAME;
import static com.sms.courier.common.field.EngineMemberField.OPEN;
import static com.sms.courier.common.field.EngineMemberField.STATUS;
import static com.sms.courier.common.field.EngineMemberField.TASK_SIZE_LIMIT;
import static com.sms.courier.docker.enmu.ContainerField.CONTAINER_STATUS;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.EngineSettingService;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.grpc.api.v1.GrpcEngineRegisterRequest;
import com.sms.courier.engine.model.EngineAddress;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.engine.request.EngineMemberRequest;
import com.sms.courier.mapper.EngineMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EngineMemberManagementImpl implements EngineMemberManagement {

    private final EngineMemberRepository engineMemberRepository;
    private final CommonRepository commonRepository;
    private final EngineMapper engineMapper;
    private final DockerService dockerService;
    private final EngineSettingService engineSettingService;
    private final JwtTokenManager jwtTokenManager;

    public EngineMemberManagementImpl(EngineMemberRepository engineMemberRepository,
        CommonRepository commonRepository,
        EngineMapper engineMapper,
        DockerService dockerService, EngineSettingService engineSettingService,
        JwtTokenManager jwtTokenManager) {
        this.engineMemberRepository = engineMemberRepository;
        this.commonRepository = commonRepository;
        this.engineMapper = engineMapper;
        this.dockerService = dockerService;
        this.engineSettingService = engineSettingService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public void registerEngine(GrpcEngineRegisterRequest request) {
        EngineMemberEntity engineMember;
        try {
            Optional<EngineMemberEntity> optional = engineMemberRepository.findFirstByName(request.getName());
            if (optional.isEmpty()) {
                EngineSettingResponse engineSetting = engineSettingService.findOne();
                engineMember = EngineMemberEntity.builder()
                    .port(request.getPort())
                    .taskSizeLimit(Objects.isNull(engineSetting.getTaskSizeLimit()) ? Integer.valueOf(-1) :
                        engineSetting.getTaskSizeLimit())
                    .status(EngineStatus.PENDING)
                    .containerStatus(ContainerStatus.START)
                    .name(request.getName())
                    .version(request.getVersion())
                    .build();
            } else {
                engineMember = optional.get();
                engineMember.setName(request.getName());
                engineMember.setContainerStatus(ContainerStatus.START);
                engineMember.setStatus(EngineStatus.PENDING);
            }
            engineMemberRepository.save(engineMember);
            log.info("Register engine : {} is success.", request.getName());
        } catch (Exception e) {
            log.error("Register engine : {} error!", request.getName(), e);
        }

    }

    @Override
    public List<EngineResponse> getRunningEngine() {
        List<EngineMemberEntity> list = engineMemberRepository
            .findAllByContainerStatusInOrderByCreateDateTimeDesc(
                List.of(ContainerStatus.START, ContainerStatus.DIE));
        return engineMapper.toResponseList(list);
    }

    @Override
    public Boolean openEngine(String id) {
        return commonRepository.updateFieldById(id, Map.of(OPEN, true), EngineMemberEntity.class);
    }

    @Override
    public Boolean closeEngine(String id) {
        return commonRepository.updateFieldById(id, Map.of(OPEN, false), EngineMemberEntity.class);
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = ENGINE_MEMBER)
    public Boolean createEngine() {
        try {
            EngineSettingResponse engineSetting = engineSettingService.findOne();
            long count = engineMemberRepository.count();
            count++;
            engineSetting.setContainerName(engineSetting.getContainerName() + "-" + count);
            engineSetting.getEnvVariable().put("TOKEN",
                jwtTokenManager.generateAccessToken(CustomUser.createEngine(engineSetting.getContainerName())));
            dockerService.startContainer(engineMapper.toContainerSetting(engineSetting));
            return true;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Create engine error!", e);
            throw ExceptionUtils.mpe(CREATE_ENGINE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = RESTART, operationModule = ENGINE_MEMBER, template = "{{#name}}")
    public Boolean restartEngine(String name) {
        try {
            dockerService.restartContainer(name);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Restart engine error!", e);
            throw ExceptionUtils.mpe(RESTART_ENGINE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = ENGINE_MEMBER, template = "{{#name}}")
    public Boolean deleteEngine(String name) {
        try {
            dockerService.deleteContainer(name);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            if (NO_SUCH_CONTAINER_ERROR.getCode().equals(e.getCode())) {
                updateEngineStatus(name);
            }
            throw e;
        } catch (Exception e) {
            log.error("Delete engine error!", e);
            throw ExceptionUtils.mpe(DELETE_ENGINE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void updateEngineStatus(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where(NAME.getName()).is(name));
        query.addCriteria(Criteria.where(STATUS.getName()).ne(EngineStatus.RUNNING));
        Update update = new Update();
        update.set(CONTAINER_STATUS.getName(), DESTROY);
        commonRepository.updateField(query, update, EngineMemberEntity.class);
    }

    @Override
    public Boolean queryLog(DockerLogRequest request) {
        dockerService.queryLog(request);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = ENGINE_MEMBER)
    public Boolean edit(EngineMemberRequest request) {
        return commonRepository.updateFieldById(request.getId(), Map.of(TASK_SIZE_LIMIT, request.getTaskSizeLimit()),
            EngineMemberEntity.class);
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = ENGINE_MEMBER, template = "{{#names}}")
    public Boolean batchDeleteEngine(List<String> names) {
        for (String name : names) {
            try {
                dockerService.deleteContainer(name);
            } catch (ApiTestPlatformException e) {
                log.error(e.getMessage());
                if (NO_SUCH_CONTAINER_ERROR.getCode().equals(e.getCode())) {
                    updateEngineStatus(name);
                }
            } catch (Exception e) {
                log.error("Batch delete engine error!", e);
            }

        }
        return Boolean.TRUE;
    }

    @Override
    public List<EngineAddress> getAvailableEngine() {
        return engineMemberRepository.findAllByContainerStatusAndOpenIsTrue(ContainerStatus.START)
            .filter(this::taskSizeLimit)
            .map(engineMember -> EngineAddress.builder().host(engineMember.getName()).port(engineMember.getPort())
                .build())
            .collect(Collectors.toList());
    }

    private boolean taskSizeLimit(EngineMemberEntity engineMemberEntity) {
        Integer taskSizeLimit = engineMemberEntity.getTaskSizeLimit();
        if (taskSizeLimit == -1) {
            return Boolean.TRUE;
        }
        return taskSizeLimit >= (engineMemberEntity.getCaseTask() + engineMemberEntity.getSceneCaseTask());
    }
}
