package com.sms.satp.service.impl;

import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.repository.LogRepository;
import com.sms.satp.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public Boolean add(LogEntity logEntity) {
        logRepository.insert(logEntity);
        return Boolean.TRUE;
    }
}
