package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.WEBHOOK_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.WebhookPageRequest;
import com.sms.courier.dto.request.WebhookRequest;
import com.sms.courier.dto.response.WebhookResponse;
import com.sms.courier.dto.response.WebhookTypeResponse;
import com.sms.courier.service.WebhookService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WEBHOOK_PATH)
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody WebhookRequest webhookRequest) {
        return webhookService.add(webhookRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody WebhookRequest webhookRequest) {
        return webhookService.edit(webhookRequest);
    }

    @PostMapping("/page")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Page<WebhookResponse> page(@RequestBody WebhookPageRequest request) {
        return webhookService.page(request);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean delete(@PathVariable List<String> ids) {
        return webhookService.delete(ids);
    }

    @GetMapping("/type")
    public List<WebhookTypeResponse> getAllType() {
        return webhookService.getAllType();
    }

    @PostMapping("/test-connection")
    public Boolean testConnection(@Validated(UpdateGroup.class) @RequestBody WebhookRequest request) {
        return webhookService.testConnection(request);
    }

}
