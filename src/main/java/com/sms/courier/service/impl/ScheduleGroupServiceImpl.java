package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCHEDULE_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCHEDULE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCHEDULE_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCHEDULE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_GROUP_LIST_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.entity.group.ScheduleGroupEntity;
import com.sms.courier.mapper.ScheduleGroupMapper;
import com.sms.courier.repository.ScheduleGroupRepository;
import com.sms.courier.service.ScheduleGroupService;
import com.sms.courier.service.ScheduleService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleGroupServiceImpl implements ScheduleGroupService {

    private final ScheduleService scheduleService;
    private final ScheduleGroupRepository scheduleGroupRepository;
    private final ScheduleGroupMapper scheduleGroupMapper;

    public ScheduleGroupServiceImpl(ScheduleService scheduleService,
        ScheduleGroupRepository scheduleGroupRepository,
        ScheduleGroupMapper scheduleGroupMapper) {
        this.scheduleService = scheduleService;
        this.scheduleGroupRepository = scheduleGroupRepository;
        this.scheduleGroupMapper = scheduleGroupMapper;
    }

    @Override
    public ScheduleGroupResponse findById(String id) {
        return scheduleGroupRepository.findById(id).map(scheduleGroupMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(GET_SCHEDULE_GROUP_BY_ID_ERROR));
    }

    @Override
    public List<ScheduleGroupResponse> list(String projectId) {
        try {
            return scheduleGroupRepository.findByProjectIdIsOrderByName(projectId);
        } catch (Exception e) {
            log.error("Failed to get the ScheduleGroup list!", e);
            throw new ApiTestPlatformException(GET_SCHEDULE_GROUP_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = SCHEDULE_GROUP, template = "{{#scheduleGroupRequest.name}}")
    public Boolean add(ScheduleGroupRequest scheduleGroupRequest) {
        log.info("ScheduleGroupService-add()-params: [ScheduleGroup]={}", scheduleGroupRequest.toString());
        try {
            ScheduleGroupEntity scheduleGroup = scheduleGroupMapper.toEntity(scheduleGroupRequest);
            scheduleGroupRepository.insert(scheduleGroup);
        } catch (Exception e) {
            log.error("Failed to add the ScheduleGroup!", e);
            throw new ApiTestPlatformException(ADD_SCHEDULE_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCHEDULE_GROUP, template = "{{#scheduleGroupRequest.name}}")
    public Boolean edit(ScheduleGroupRequest scheduleGroupRequest) {
        log.info("ScheduleGroupService-edit()-params: [ScheduleGroup]={}", scheduleGroupRequest.toString());
        try {
            boolean exists = scheduleGroupRepository.existsById(scheduleGroupRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "ScheduleGroup", scheduleGroupRequest.getId());
            }
            ScheduleGroupEntity scheduleGroup = scheduleGroupMapper.toEntity(scheduleGroupRequest);
            scheduleGroupRepository.save(scheduleGroup);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the ScheduleGroup!", e);
            throw new ApiTestPlatformException(EDIT_SCHEDULE_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.DELETE, operationModule = SCHEDULE_GROUP, template = "{{#result.name}}",
        enhance = @Enhance(enable = true))
    public Boolean delete(String id) {
        try {
            scheduleGroupRepository.deleteById(id);
            scheduleService.deleteByGroupId(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the ScheduleGroup!", e);
            throw new ApiTestPlatformException(DELETE_SCHEDULE_GROUP_BY_ID_ERROR);
        }
    }

}
