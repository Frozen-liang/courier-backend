package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_SCHEDULE_RECORD_PAGE_ERROR;
import static com.sms.courier.common.field.CommonField.PROJECT_ID;
import static com.sms.courier.common.field.ScheduleRecordField.EXECUTE;
import static com.sms.courier.common.field.ScheduleRecordField.FAIL;
import static com.sms.courier.common.field.ScheduleRecordField.SCHEDULE_ID;
import static com.sms.courier.common.field.ScheduleRecordField.SCHEDULE_NAME;
import static com.sms.courier.common.field.ScheduleRecordField.SUCCESS;
import static com.sms.courier.common.field.ScheduleRecordField.VERSION;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ScheduleRecordPageRequest;
import com.sms.courier.dto.response.ScheduleRecordResponse;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.ScheduleRecordMapper;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.service.ScheduleRecordService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.PageDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleRecordServiceImpl implements ScheduleRecordService {

    private final ScheduleRecordRepository recordRepository;
    private final ScheduleRecordMapper scheduleRecordMapper;

    public ScheduleRecordServiceImpl(ScheduleRecordRepository recordRepository,
        ScheduleRecordMapper scheduleRecordMapper) {
        this.recordRepository = recordRepository;
        this.scheduleRecordMapper = scheduleRecordMapper;
    }

    @Override
    public Page<ScheduleRecordResponse> page(ScheduleRecordPageRequest request) {
        try {
            PageDtoConverter.frontMapping(request);
            Pageable pageable = PageDtoConverter.createPageable(request);
            ScheduleRecordEntity scheduleRecordEntity = scheduleRecordMapper.toEntity(request);
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(SCHEDULE_NAME.getName(), GenericPropertyMatchers.contains())
                .withMatcher(PROJECT_ID.getName(), GenericPropertyMatchers.exact())
                .withMatcher(SCHEDULE_ID.getName(), GenericPropertyMatchers.exact())
                .withMatcher(EXECUTE.getName(), GenericPropertyMatchers.exact())
                .withIgnorePaths(SUCCESS.getName(), FAIL.getName(), VERSION.getName())
                .withIgnoreNullValues();
            Example<ScheduleRecordEntity> example = Example.of(scheduleRecordEntity, exampleMatcher);
            Page<ScheduleRecordEntity> page = recordRepository.findAll(example, pageable);
            return page.map(scheduleRecordMapper::toResponse);
        } catch (Exception e) {
            log.error("Failed to get the ScheduleRecord page!", e);
            throw ExceptionUtils.mpe(GET_SCHEDULE_RECORD_PAGE_ERROR);
        }
    }
}
