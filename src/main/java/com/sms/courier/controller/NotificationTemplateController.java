package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.NOTIFICATION_TEMPLATE_PATH;

import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.service.NotificationTemplateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(NOTIFICATION_TEMPLATE_PATH)
public class NotificationTemplateController {

    private final NotificationTemplateService notificationTemplateService;

    public NotificationTemplateController(
        NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

    @PostMapping
    public Boolean save(@RequestBody NotificationTemplateRequest request) {
        return notificationTemplateService.save(request);
    }
}