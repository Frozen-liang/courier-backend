package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SYSTEM_VERSION_PATE;

import com.sms.courier.dto.response.SystemVersionResponse;
import com.sms.courier.service.SystemVersionService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SYSTEM_VERSION_PATE)
public class SystemVersionController {

    private final SystemVersionService systemVersionService;

    public SystemVersionController(SystemVersionService systemVersionService) {
        this.systemVersionService = systemVersionService;
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public List<SystemVersionResponse> findAll() {
        return systemVersionService.findAll();
    }

}
