package com.sms.courier.engine.impl;

import static com.sms.courier.common.field.EngineMemberField.CASE_TASK;
import static com.sms.courier.common.field.EngineMemberField.DESTINATION;
import static com.sms.courier.common.field.EngineMemberField.OPEN;
import static com.sms.courier.common.field.EngineMemberField.SCENE_CASE_TASK;
import static com.sms.courier.common.field.EngineMemberField.TASK_COUNT;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.CaseRecordRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.EngineId;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.enums.EngineStatus;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import com.sms.courier.engine.task.SuspiciousEngineManagement;
import com.sms.courier.mapper.EngineMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.EngineMemberRepository;
import com.sms.courier.utils.ExceptionUtils;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
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

    public EngineMemberManagementImpl(EngineMemberRepository engineMemberRepository,
        CommonRepository commonRepository,
        SuspiciousEngineManagement suspiciousEngineManagement, EngineMapper engineMapper) {
        this.engineMemberRepository = engineMemberRepository;
        this.commonRepository = commonRepository;
        this.suspiciousEngineManagement = suspiciousEngineManagement;
        this.engineMapper = engineMapper;
    }

    @Override
    public String bind(EngineRegistrationRequest request) {
        EngineMemberEntity engineMember = EngineMemberEntity.builder()
            .destination(EngineId.generate())
            .host(request.getHost())
            .status(EngineStatus.PENDING)
            .name(request.getName())
            .version(request.getVersion())
            .build();
        engineMemberRepository.insert(engineMember);
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
        List<EngineMemberEntity> list = engineMemberRepository.findAllByStatus(EngineStatus.RUNNING)
            .collect(Collectors.toList());
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
        if (StringUtils.isBlank(destination) || !destination.startsWith("/engine") || !destination.endsWith("/invoke")) {
            return;
        }
        EngineMemberEntity engineMember = engineMemberRepository.findFirstByDestination(destination)
            .orElse(new EngineMemberEntity());
        if (engineMember.getStatus() == EngineStatus.WAITING_FOR_RECONNECTION) {
            suspiciousEngineManagement.remove(engineMember.getDestination());
            log.info("The Engine reconnection.destination:{}", engineMember.getDestination());
        }
        engineMember.setStatus(EngineStatus.RUNNING);
        engineMember.setDestination(destination);
        engineMember.setSessionId(sessionId);
        engineMemberRepository.save(engineMember);
        log.info("The test engine {} activated.", destination);
    }

    private boolean taskSizeLimit(EngineMemberEntity engineMemberEntity) {
        Integer taskSizeLimit = engineMemberEntity.getTaskSizeLimit();
        if (taskSizeLimit == -1) {
            return Boolean.TRUE;
        }
        return taskSizeLimit >= (engineMemberEntity.getCaseTask() + engineMemberEntity.getSceneCaseTask());
    }
}
