package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCHEDULE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.ScheduleStatusType.CREATE;
import static com.sms.courier.common.enums.ScheduleStatusType.UPDATE;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_LIST_ERROR;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.ScheduleField.SCHEDULE_STATUS;
import static com.sms.courier.common.field.ScheduleField.TASK_STATUS;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.enums.ScheduleStatusType;
import com.sms.courier.common.enums.TaskStatus;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.Field;
import com.sms.courier.dto.request.ScheduleListRequest;
import com.sms.courier.dto.request.ScheduleRequest;
import com.sms.courier.dto.response.ScheduleResponse;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.mapper.ScheduleMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleRepository;
import com.sms.courier.service.ScheduleCaseJobService;
import com.sms.courier.service.ScheduleSceneCaseJobService;
import com.sms.courier.service.ScheduleService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommonRepository commonRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleCaseJobService scheduleCaseJobService;
    private final ScheduleSceneCaseJobService scheduleSceneCaseJobService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
        CommonRepository commonRepository, ScheduleMapper scheduleMapper,
        ScheduleCaseJobService scheduleCaseJobService,
        ScheduleSceneCaseJobService scheduleSceneCaseJobService) {
        this.scheduleRepository = scheduleRepository;
        this.commonRepository = commonRepository;
        this.scheduleMapper = scheduleMapper;
        this.scheduleCaseJobService = scheduleCaseJobService;
        this.scheduleSceneCaseJobService = scheduleSceneCaseJobService;
    }

    @Override
    public ScheduleResponse findById(String id) {
        return scheduleRepository.findById(id).map(scheduleMapper::toResponse)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_SCHEDULE_BY_ID_ERROR));
    }

    @Override
    public List<ScheduleResponse> list(ScheduleListRequest request) {
        try {
            return commonRepository.listLookupUser(CollectionName.SCHEDULE.getName(),
                List.of(REMOVE.is(true)),
                ScheduleResponse.class);
        } catch (Exception e) {
            log.error("Failed to get Schedule list.");
            throw ExceptionUtils.mpe(GET_SCHEDULE_LIST_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCHEDULE, template = "{{#request.name}}")
    public Boolean add(ScheduleRequest request) {
        try {
            ScheduleEntity schedule = scheduleMapper.toEntity(request);
            schedule.setScheduleStatus(CREATE);
            scheduleRepository.insert(schedule);
        } catch (Exception e) {
            log.error("Failed to add Schedule. message:{}", e.getMessage());
            throw ExceptionUtils.mpe(ADD_SCHEDULE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCHEDULE, template = "{{#request.name}}")
    public Boolean edit(ScheduleRequest request) {
        try {
            ScheduleEntity oldSchedule = scheduleRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Schedule", request.getId()));
            ScheduleEntity schedule = scheduleMapper.toEntity(request);
            if (checkScheduleTime(oldSchedule, schedule)) {
                schedule.setScheduleStatus(UPDATE);
            }
            scheduleRepository.save(schedule);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add Schedule.", e);
            throw ExceptionUtils.mpe(EDIT_SCHEDULE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.DELETE, operationModule = SCHEDULE, template = "{{#result.name}}",
        enhance = @Enhance(enable = true))
    public Boolean delete(String id) {
        try {
            Map<Field, Object> updateFields = new HashMap<>();
            updateFields.put(REMOVE, true);
            updateFields.put(SCHEDULE_STATUS, ScheduleStatusType.DELETE);
            return commonRepository.updateFieldById(id, updateFields, ScheduleEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete Schedule. message:{}", e.getMessage());
            throw ExceptionUtils.mpe(DELETE_SCHEDULE_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean handle(String id) {
        try {
            ScheduleEntity scheduleEntity = scheduleRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(GET_SCHEDULE_BY_ID_ERROR));
            CaseType caseType = scheduleEntity.getCaseType();
            Query query = Query.query(Criteria.where(ID.getName()).is(id));
            Update update = new Update();
            update.set(TASK_STATUS.getName(), TaskStatus.RUNNING);
            commonRepository.updateField(query, update, ScheduleEntity.class);
            boolean result = false;
            switch (caseType) {
                case CASE:
                    scheduleCaseJobService.schedule(scheduleEntity);
                    result = true;
                    break;
                case SCENE_CASE:
                    scheduleSceneCaseJobService.schedule(scheduleEntity);
                    result = true;
                    break;
            }
            return result;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Handle schedule error.", e);
        }
        return false;
    }

    private boolean checkScheduleTime(ScheduleEntity oldSchedule, ScheduleEntity newSchedule) {
        if (oldSchedule.isLoop() != newSchedule.isLoop()) {
            return true;
        }
        if (oldSchedule.getCycle() != newSchedule.getCycle()) {
            return true;
        }
        if (!Objects.equals(oldSchedule.getTime(), newSchedule.getTime())) {
            return true;
        }
        return !Objects.equals(oldSchedule.getWeek(), newSchedule.getWeek());
    }
}
