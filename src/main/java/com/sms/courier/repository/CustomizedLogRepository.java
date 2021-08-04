package com.sms.courier.repository;

import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.entity.log.LogEntity;
import org.springframework.data.domain.Page;

public interface CustomizedLogRepository {

    Page<LogEntity> page(LogPageRequest logPageRequest);
}
