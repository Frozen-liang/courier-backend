package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.EMAIL_SERVICE_PATH;

import com.sms.courier.dto.request.EmailRequest;
import com.sms.courier.service.EmailSettingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EMAIL_SERVICE_PATH)
public class EmailServiceController {

    private final EmailSettingService emailSettingService;

    public EmailServiceController(EmailSettingService emailSettingService) {
        this.emailSettingService = emailSettingService;
    }

    @PostMapping
    public boolean updateEmailProperty(@Validated @RequestBody EmailRequest emailRequest) {
        return emailSettingService.updateEmailConfiguration(emailRequest);
    }

    @PostMapping("/enable")
    public boolean enable() {
        return emailSettingService.enable();
    }

    @PostMapping("/disable")
    public boolean disable() {
        return emailSettingService.disable();
    }
}
