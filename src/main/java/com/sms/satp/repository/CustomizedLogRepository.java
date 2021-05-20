package com.sms.satp.repository;

import com.sms.satp.dto.request.LogPageRequest;
import com.sms.satp.entity.log.LogEntity;
import org.springframework.data.domain.Page;

public interface CustomizedLogRepository {

    Page<LogEntity> page(LogPageRequest logPageRequest);
}
