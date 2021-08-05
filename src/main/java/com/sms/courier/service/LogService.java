package com.sms.courier.service;

import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.dto.response.LogResponse;
import com.sms.courier.entity.log.LogEntity;
import org.springframework.data.domain.Page;

public interface LogService {

    Boolean add(LogEntity logEntity);

    Page<LogResponse> page(LogPageRequest logPageRequest);
}
