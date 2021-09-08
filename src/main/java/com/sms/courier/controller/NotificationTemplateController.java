package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.NOTIFICATION_TEMPLATE_PATH;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.dto.request.NotificationTemplateRequest;
import com.sms.courier.dto.response.NotificationTemplateResponse;
import com.sms.courier.dto.response.NotificationTemplateTypeResponse;
import com.sms.courier.service.NotificationTemplateService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/type")
    public List<NotificationTemplateTypeResponse> getTemplateType() {
        return NotificationTemplateType.getResponse();
    }

    @GetMapping("/{typeId}")
    public NotificationTemplateResponse getTemplate(@PathVariable Integer typeId) {
        return notificationTemplateService.getResponseByType(typeId);
    }

    @PostMapping
    public Boolean save(@RequestBody NotificationTemplateRequest request) {
        return notificationTemplateService.save(request);
    }
}