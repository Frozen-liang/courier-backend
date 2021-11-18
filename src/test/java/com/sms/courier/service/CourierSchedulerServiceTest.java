package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.EDIT_COURIER_SCHEDULER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.CourierSchedulerRequest;
import com.sms.courier.dto.response.CourierSchedulerResponse;
import com.sms.courier.entity.schedule.CourierSchedulerEntity;
import com.sms.courier.mapper.CourierSchedulerMapper;
import com.sms.courier.repository.CourierSchedulerRepository;
import com.sms.courier.service.impl.CourierSchedulerServiceImpl;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for CourierSchedulerService")
public class CourierSchedulerServiceTest {

    private final DockerService dockerService = mock(DockerService.class);
    private final CourierSchedulerRepository courierSchedulerRepository = mock(CourierSchedulerRepository.class);
    private final CourierSchedulerMapper courierSchedulerMapper = mock(CourierSchedulerMapper.class);
    private final CourierSchedulerService courierSchedulerService =
        new CourierSchedulerServiceImpl(courierSchedulerRepository, dockerService, courierSchedulerMapper);
    private final static String ID = new ObjectId().toString();

    @Test
    @DisplayName("Test the edit method in the CourierScheduler service")
    public void edit_test() {
        CourierSchedulerRequest request = CourierSchedulerRequest.builder().build();
        CourierSchedulerEntity courierSchedulerEntity = CourierSchedulerEntity.builder().id(ID).build();
        Optional<CourierSchedulerEntity> optional = Optional.ofNullable(courierSchedulerEntity);
        when(courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc()).thenReturn(optional);
        when(courierSchedulerRepository.save(any())).thenReturn(courierSchedulerEntity);
        Boolean isSuccess = courierSchedulerService.edit(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while edit CourierScheudler")
    public void edit_exception_test() {
        CourierSchedulerRequest request = CourierSchedulerRequest.builder().build();
        CourierSchedulerEntity courierSchedulerEntity = CourierSchedulerEntity.builder().id(ID).build();
        Optional<CourierSchedulerEntity> optional = Optional.ofNullable(courierSchedulerEntity);
        when(courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc()).thenThrow(new RuntimeException());
        when(courierSchedulerRepository.save(any())).thenReturn(courierSchedulerEntity);
        assertThatThrownBy(() -> courierSchedulerService.edit(request)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_COURIER_SCHEDULER_ERROR.getCode());

    }

    @Test
    @DisplayName("Test the findOne method in the CourierScheduler service")
    public void findOne_test() {
        when(courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc()).thenReturn(Optional.empty());
        CourierSchedulerResponse result = courierSchedulerService.findOne();
        assertThat(result).isNotNull();
        assertThat(result.getContainerName()).isBlank();
    }

    @Test
    public void createCourierScheduler_test() {
        CourierSchedulerEntity courierSchedulerEntity = CourierSchedulerEntity.builder().id(ID).build();
        Optional<CourierSchedulerEntity> optional = Optional.ofNullable(courierSchedulerEntity);
        when(courierSchedulerRepository.findFirstByOrderByCreateDateTimeDesc()).thenReturn(optional);
        doNothing().when(dockerService).startContainer(any());
        when(courierSchedulerRepository.save(any())).thenReturn(courierSchedulerEntity);
        Boolean isSuccess = courierSchedulerService.createCourierScheduler();
        assertTrue(isSuccess);
    }

    @Test
    public void restartCourierScheduler_test() {
        doNothing().when(dockerService).restartContainer(any());
        Boolean isSuccess = courierSchedulerService.restartCourierScheduler();
        assertTrue(isSuccess);
    }

    @Test
    public void deleteCourierScheduler_test() {
        doNothing().when(dockerService).deleteContainer(any());
        Boolean isSuccess = courierSchedulerService.deleteCourierScheduler();
        assertTrue(isSuccess);
    }
}
