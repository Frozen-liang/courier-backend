package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.COURIER_SCHEDULER;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.RESTART;
import static com.sms.courier.common.exception.ErrorCode.EDIT_COURIER_SCHEDULER_ERROR;

import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.CourierSchedulerRequest;
import com.sms.courier.dto.response.CourierSchedulerResponse;
import com.sms.courier.entity.schedule.CourierSchedulerEntity;
import com.sms.courier.mapper.CourierSchedulerMapper;
import com.sms.courier.repository.CourierSchedulerRepository;
import com.sms.courier.service.CourierSchedulerService;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourierSchedulerServiceImpl implements CourierSchedulerService {

    private final CourierSchedulerRepository courierSchedulerRepository;
    private final DockerService dockerService;
    private final CourierSchedulerMapper courierSchedulerMapper;


    public CourierSchedulerServiceImpl(CourierSchedulerRepository courierSchedulerRepository,
        DockerService dockerService, CourierSchedulerMapper courierSchedulerMapper) {
        this.courierSchedulerRepository = courierSchedulerRepository;
        this.dockerService = dockerService;
        this.courierSchedulerMapper = courierSchedulerMapper;
    }


    @Override
    @LogRecord(operationType = OperationType.EDIT, operationModule = COURIER_SCHEDULER)
    public Boolean edit(CourierSchedulerRequest request) {
        try {
            CourierSchedulerEntity courierSchedulerEntity =
                courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc().orElse(new CourierSchedulerEntity());
            courierSchedulerEntity.setImageName(request.getImageName());
            courierSchedulerEntity.setContainerName(Constants.COURIER_SCHEDULE_CONTAINER_NAME);
            courierSchedulerEntity.setVersion(request.getVersion());
            courierSchedulerEntity.setEnvVariable(request.getEnvVariable());
            courierSchedulerRepository.save(courierSchedulerEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CourierSchedulerSetting!", e);
            throw ExceptionUtils.mpe(EDIT_COURIER_SCHEDULER_ERROR);
        }
    }

    @Override
    public CourierSchedulerResponse findOne() {
        return courierSchedulerRepository.getFirstByOrderByCreateDateTimeDesc()
            .orElse(new CourierSchedulerResponse());
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = COURIER_SCHEDULER)
    public Boolean createCourierScheduler() {
        CourierSchedulerEntity courierScheduler = courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc()
            .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_COURIER_SCHEDULER_ERROR));
        dockerService.startContainer(courierSchedulerMapper.toContainerSetting(courierScheduler));
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = RESTART, operationModule = COURIER_SCHEDULER)
    public Boolean restartCourierScheduler() {
        dockerService.restartContainer(Constants.COURIER_SCHEDULE_CONTAINER_NAME);
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = COURIER_SCHEDULER)
    public Boolean deleteCourierScheduler() {
        dockerService.deleteContainer(Constants.COURIER_SCHEDULE_CONTAINER_NAME);
        return Boolean.TRUE;
    }


}
