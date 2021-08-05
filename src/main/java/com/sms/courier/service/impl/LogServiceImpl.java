package com.sms.courier.service.impl;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.dto.response.LogResponse;
import com.sms.courier.entity.log.LogEntity;
import com.sms.courier.mapper.LogMapper;
import com.sms.courier.repository.CustomizedLogRepository;
import com.sms.courier.repository.LogRepository;
import com.sms.courier.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final CustomizedLogRepository customizedLogRepository;
    private final LogMapper logMapper;

    public LogServiceImpl(LogRepository logRepository,
        CustomizedLogRepository customizedLogRepository, LogMapper logMapper) {
        this.logRepository = logRepository;
        this.customizedLogRepository = customizedLogRepository;
        this.logMapper = logMapper;
    }

    @Override
    public Boolean add(LogEntity logEntity) {
        try {
            logRepository.insert(logEntity);
        } catch (Exception e) {
            log.error("Failed to add the log. {}", logEntity.toString());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Page<LogResponse> page(LogPageRequest logPageRequest) {
        try {
            return customizedLogRepository.page(logPageRequest).map(logMapper::toDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiTestPlatformException(ErrorCode.GET_LOG_PAGE_ERROR);
        }
    }
}
