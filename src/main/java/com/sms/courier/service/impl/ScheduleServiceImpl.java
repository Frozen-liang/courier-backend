package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCHEDULE;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.ScheduleStatusType.CREATE;
import static com.sms.courier.common.enums.ScheduleStatusType.UPDATE;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.CASE_TYPE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_CASE_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCHEDULE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.SYSTEM_ERROR;
import static com.sms.courier.common.field.CommonField.GROUP_ID;
import static com.sms.courier.common.field.CommonField.ID;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.CommonField.REMOVE;
import static com.sms.courier.common.field.ScheduleField.NAME;
import static com.sms.courier.common.field.ScheduleField.OPEN;
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
import com.sms.courier.repository.CustomizedScheduleRepository;
import com.sms.courier.repository.ScheduleRepository;
import com.sms.courier.service.ScheduleCaseJobService;
import com.sms.courier.service.ScheduleSceneCaseJobService;
import com.sms.courier.service.ScheduleService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
    private final CustomizedScheduleRepository customizedScheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
        CommonRepository commonRepository, ScheduleMapper scheduleMapper,
        ScheduleCaseJobService scheduleCaseJobService,
        ScheduleSceneCaseJobService scheduleSceneCaseJobService,
        CustomizedScheduleRepository customizedScheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.commonRepository = commonRepository;
        this.scheduleMapper = scheduleMapper;
        this.scheduleCaseJobService = scheduleCaseJobService;
        this.scheduleSceneCaseJobService = scheduleSceneCaseJobService;
        this.customizedScheduleRepository = customizedScheduleRepository;
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
                List.of(REMOVE.is(false), PROJECT_ID.is(request.getProjectId()), GROUP_ID.is(request.getGroupId()),
                    NAME.like(request.getName())),
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
    @LogRecord(operationType = EDIT, operationModule = SCHEDULE, template = "{{#request.name}}",
        sourceId = "{{#request.id}}")
    public Boolean edit(ScheduleRequest request) {
        try {
            ScheduleEntity oldSchedule = scheduleRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "Schedule", request.getId()));
            ScheduleEntity schedule = scheduleMapper.toEntity(request);
            if (checkScheduleTime(oldSchedule, schedule)) {
                schedule.setScheduleStatus(UPDATE);
            }
            schedule.setLastTaskCompleteTime(oldSchedule.getLastTaskCompleteTime());
            schedule.setTaskStatus(oldSchedule.getTaskStatus());
            schedule.setOpen(oldSchedule.isOpen());
            scheduleRepository.save(schedule);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to edit Schedule.", e);
            throw ExceptionUtils.mpe(EDIT_SCHEDULE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.DELETE, operationModule = SCHEDULE,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"),
        sourceId = "{{#ids}}")
    public Boolean delete(List<String> ids) {
        try {
            Map<Field, Object> updateFields = new HashMap<>();
            updateFields.put(REMOVE, true);
            updateFields.put(SCHEDULE_STATUS, ScheduleStatusType.DELETE);
            return commonRepository.updateFieldByIds(ids, updateFields, ScheduleEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete Schedule. message:{}", e.getMessage());
            throw ExceptionUtils.mpe(DELETE_SCHEDULE_BY_ID_ERROR);
        }
    }


    @Override
    public void deleteByGroupId(String groupId) {
        List<String> ids = scheduleRepository.findByGroupId(groupId).map(ScheduleEntity::getId)
            .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return;
        }
        this.delete(ids);
    }

    @Override
    public Boolean handle(String id, String metadata) {
        try {
            ScheduleEntity scheduleEntity = scheduleRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(GET_SCHEDULE_BY_ID_ERROR));
            CaseType caseType = scheduleEntity.getCaseType();
            Query query = Query.query(Criteria.where(ID.getName()).is(id));
            Update update = new Update();
            update.set(TASK_STATUS.getName(), TaskStatus.RUNNING);
            commonRepository.updateField(query, update, ScheduleEntity.class);
            switch (caseType) {
                case CASE:
                    scheduleCaseJobService.schedule(scheduleEntity, metadata);
                    break;
                case SCENE_CASE:
                    scheduleSceneCaseJobService.schedule(scheduleEntity, metadata);
                    break;
                default:
                    throw ExceptionUtils.mpe(CASE_TYPE_ERROR);
            }
            return true;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Handle schedule error.", e);
            throw new ApiTestPlatformException(SYSTEM_ERROR);
        }
    }

    @Override
    public Boolean open(String id, boolean enable) {
        try {
            Map<Field, Object> updateFields = new HashMap<>();
            updateFields.put(OPEN, enable);
            updateFields.put(SCHEDULE_STATUS, UPDATE);
            return commonRepository.updateFieldById(id, updateFields, ScheduleEntity.class);
        } catch (Exception e) {
            log.error("Failed to update Schedule status.", e);
            throw ExceptionUtils.mpe(EDIT_SCHEDULE_ERROR);
        }
    }

    @Override
    public Boolean removeCaseIds(List<String> caseIds) {
        try {
            return customizedScheduleRepository.removeCaseIds(caseIds);
        } catch (Exception e) {
            log.error("Failed to delete the Schedule case id.", e);
            throw ExceptionUtils.mpe(DELETE_SCHEDULE_CASE_ID_ERROR);
        }
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
