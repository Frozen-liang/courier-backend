package com.sms.satp.service.impl;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.exception.ErrorCode;
import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.dto.response.LogResponse;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.mapper.LogMapper;
import com.sms.satp.repository.CustomizedLogRepository;
import com.sms.satp.repository.LogRepository;
import com.sms.satp.service.LogService;
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
            log.error("Failed to add the log. logEntity{}", logEntity.toString());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Page<LogResponse> page(LogPageRequest logPageRequest) {
        try {
            return customizedLogRepository.page(logPageRequest).map(logMapper::toDto);
        } catch (Exception e) {
            throw new ApiTestPlatformException(ErrorCode.GET_LOG_PAGE_ERROR);
        }
    }
}
