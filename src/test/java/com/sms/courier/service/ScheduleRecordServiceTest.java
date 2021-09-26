package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_RECORD_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ScheduleRecordPageRequest;
import com.sms.courier.dto.response.ScheduleRecordResponse;
import com.sms.courier.mapper.ScheduleRecordMapper;
import com.sms.courier.mapper.ScheduleRecordMapperImpl;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.service.impl.ScheduleRecordServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DisplayName("Tests for ScheduleRecordService")
public class ScheduleRecordServiceTest {

    private final ScheduleRecordRepository recordRepository = mock(ScheduleRecordRepository.class);
    private final ScheduleRecordMapper scheduleRecordMapper = new ScheduleRecordMapperImpl();
    private final ScheduleRecordService scheduleRecordService =
        new ScheduleRecordServiceImpl(recordRepository, scheduleRecordMapper);

    @Test
    @DisplayName("Test the page method in the ScheduleRecord service")
    public void page_test() {
        ScheduleRecordPageRequest request = ScheduleRecordPageRequest.builder().build();
        when(recordRepository.findAll(any(), any(Pageable.class))).thenReturn(Page.empty());
        Page<ScheduleRecordResponse> page = scheduleRecordService.page(request);
        assertThat(page).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while get ScheduleRecord page")
    public void page_exception_test() {
        ScheduleRecordPageRequest request = ScheduleRecordPageRequest.builder().build();
        when(recordRepository.findAll(any(), any(Pageable.class))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> scheduleRecordService.page(request)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_SCHEDULE_RECORD_PAGE_ERROR.getCode());

    }
}
