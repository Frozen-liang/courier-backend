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
import static com.sms.courier.common.field.EngineMemberField.CASE_TASK;
import static com.sms.courier.common.field.EngineMemberField.DESTINATION;
import static com.sms.courier.common.field.EngineMemberField.NAME;
import static com.sms.courier.common.field.EngineMemberField.OPEN;
import static com.sms.courier.common.field.EngineMemberField.SCENE_CASE_TASK;
import static com.sms.courier.common.field.EngineMemberField.STATUS;
import static com.sms.courier.common.field.EngineMemberField.TASK_COUNT;
import static com.sms.courier.common.field.EngineMemberField.TASK_SIZE_LIMIT;
import static com.sms.courier.docker.enmu.ContainerField.CONTAINER_STATUS;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineId;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.EngineSettingService;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.engine.request.EngineMemberRequest;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import com.sms.courier.engine.task.SuspiciousEngineManagement;
import com.sms.courier.mapper.EngineMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.utils.ExceptionUtils;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class EngineMemberManagementImpl implements EngineMemberManagement {

    private final SecureRandom random = new SecureRandom();
    private final EngineMemberRepository engineMemberRepository;
    private final CommonRepository commonRepository;
    private final SuspiciousEngineManagement suspiciousEngineManagement;
    private final EngineMapper engineMapper;
    private final DockerService dockerService;
    private final EngineSettingService engineSettingService;

    public EngineMemberManagementImpl(EngineMemberRepository engineMemberRepository,
        CommonRepository commonRepository,
        SuspiciousEngineManagement suspiciousEngineManagement, EngineMapper engineMapper,
        DockerService dockerService, EngineSettingService engineSettingService) {
        this.engineMemberRepository = engineMemberRepository;
        this.commonRepository = commonRepository;
        this.suspiciousEngineManagement = suspiciousEngineManagement;
        this.engineMapper = engineMapper;
        this.dockerService = dockerService;
        this.engineSettingService = engineSettingService;
    }

    @Override
    public String bind(EngineRegistrationRequest request) {
        EngineMemberEntity engineMember;
        Optional<EngineMemberEntity> optional = engineMemberRepository.findFirstByName(request.getName());
        if (optional.isEmpty()) {
            EngineSettingResponse engineSetting = engineSettingService.findOne();
            engineMember = EngineMemberEntity.builder()
                .destination(EngineId.generate())
                .host(request.getHost())
                .taskSizeLimit(Objects.isNull(engineSetting.getTaskSizeLimit()) ? Integer.valueOf(-1) :
                    engineSetting.getTaskSizeLimit())
                .status(EngineStatus.PENDING)
                .name(request.getName())
                .version(request.getVersion())
                .build();
        } else {
            engineMember = optional.get();
            engineMember.setDestination(EngineId.generate());
            engineMember.setStatus(EngineStatus.PENDING);
        }
        engineMemberRepository.save(engineMember);
        log.info("The destination {} of the test engine is binding.", engineMember.getDestination());
        return engineMember.getDestination();
    }


    @Override
    public String getAvailableMember() throws ApiTestPlatformException {
        List<String> availableMembers = engineMemberRepository.findAllByStatusAndOpenIsTrue(EngineStatus.RUNNING)
            .filter(this::taskSizeLimit)
            .map(EngineMemberEntity::getDestination)
            .collect(Collectors.toUnmodifiableList());
        if (CollectionUtils.isEmpty(availableMembers)) {
            throw ExceptionUtils.mpe("No engines are available.");
        }
        return availableMembers.get(random.nextInt(availableMembers.size()));
    }

    @Override
    public void caseRecord(CaseRecordRequest caseRecordRequest) {
        String destination = caseRecordRequest.getDestination();
        Integer caseCount = caseRecordRequest.getCaseCount();
        Integer sceneCaseCount = caseRecordRequest.getSceneCaseCount();
        log.info("The destination {}  caseTask {} sceneCaseTask {}.", destination,
            caseCount, sceneCaseCount);
        Query query = Query.query(Criteria.where(DESTINATION.getName()).is(destination));
        Update update = new Update();
        update.set(CASE_TASK.getName(), caseCount);
        update.set(SCENE_CASE_TASK.getName(), sceneCaseCount);
        commonRepository.updateField(query, update, EngineMemberEntity.class);

    }

    @Override
    public void countTaskRecord(String destination, Integer size) {
        Query query = Query.query(Criteria.where(DESTINATION.getName()).is(destination));
        Update update = new Update();
        update.inc(TASK_COUNT.getName(), size);
        commonRepository.updateField(query, update, EngineMemberEntity.class);
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
    public void unBind(String sessionId) {
        Optional<EngineMemberEntity> engineMemberOptional = engineMemberRepository.findFirstBySessionId(sessionId);
        engineMemberOptional.ifPresent((engineMember -> {
            engineMember.setStatus(EngineStatus.WAITING_FOR_RECONNECTION);
            engineMemberRepository.save(engineMember);
            suspiciousEngineManagement.add(engineMember.getDestination());
            log.info("The destination {} unbind from member of the engine.", engineMember.getDestination());
        }));
    }

    @Override
    public void active(String sessionId, String destination) {
        if (isEngineDestination(destination)) {
            engineMemberRepository.findFirstByDestination(destination).ifPresent(engineMember -> {
                suspiciousEngineManagement.remove(engineMember.getDestination());
                engineMember.setStatus(EngineStatus.RUNNING);
                engineMember.setDestination(destination);
                engineMember.setSessionId(sessionId);
                engineMemberRepository.save(engineMember);
                log.info("The test engine {} activated.", destination);
            });
        }
    }

    private boolean isEngineDestination(String destination) {
        return StringUtils.isNotBlank(destination) && destination.startsWith("/engine")
            && destination.endsWith("/invoke");
    }

    private boolean taskSizeLimit(EngineMemberEntity engineMemberEntity) {
        Integer taskSizeLimit = engineMemberEntity.getTaskSizeLimit();
        if (taskSizeLimit == -1) {
            return Boolean.TRUE;
        }
        return taskSizeLimit >= (engineMemberEntity.getCaseTask() + engineMemberEntity.getSceneCaseTask());
    }
}
