package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.EMAIL_SERVICE_PATH;

import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.dto.response.EmailPropertiesResponse;
import com.sms.courier.service.EmailService;
import com.sms.courier.service.EmailSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EMAIL_SERVICE_PATH)
public class EmailServiceController {

    private final EmailService emailService;
    private final EmailSettingService emailSettingService;

    public EmailServiceController(EmailService emailService,
        EmailSettingService emailSettingService) {
        this.emailService = emailService;
        this.emailSettingService = emailSettingService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public boolean updateEmailProperty(@Validated @RequestBody EmailRequest emailRequest) {
        return emailSettingService.updateEmailConfiguration(emailRequest);
    }

    @GetMapping("/properties")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public EmailPropertiesResponse getPropertiesResponse() {
        return emailService.getEmailConfigurationResponse();
    }

    @GetMapping("/status")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public boolean getServiceStatus() {
        return emailService.isServiceEnabled();
    }

    @PostMapping("/enable")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public boolean enable() {
        return emailSettingService.enable();
    }

    @PostMapping("/disable")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public boolean disable() {
        return emailSettingService.disable();
    }
}
