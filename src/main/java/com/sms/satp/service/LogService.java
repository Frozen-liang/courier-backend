package com.sms.satp.service;

import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.dto.response.LogResponse;
import com.sms.satp.entity.log.LogEntity;
import org.springframework.data.domain.Page;

public interface LogService {

    Boolean add(LogEntity logEntity);

    Page<LogResponse> page(LogPageRequest logPageRequest);
}
