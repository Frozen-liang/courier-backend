package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.LogPageRequest;
import com.sms.courier.dto.response.LogResponse;
import com.sms.courier.service.LogService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.LOG_PATH)
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/page")
    @PreAuthorize("hasRoleOrAdmin(@role.LOG_QUERY_ALL)")
    public Page<LogResponse> page(LogPageRequest logPageRequest) {
        return logService.page(logPageRequest);
    }

}
