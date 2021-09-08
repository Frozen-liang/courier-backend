package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.EMAIL_SETTINGS_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.EmailSettingsRequest;
import com.sms.courier.dto.response.EmailSettingsResponse;
import com.sms.courier.service.EmailSettingsService;
import java.util.List;
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
@RequestMapping(EMAIL_SETTINGS_PATH)
public class EmailSettingsController {

    private final EmailSettingsService emailSettingsService;

    public EmailSettingsController(EmailSettingsService emailSettingsService) {
        this.emailSettingsService = emailSettingsService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public EmailSettingsResponse getById(@PathVariable("id") String id) {
        return emailSettingsService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody EmailSettingsRequest emailSettingsRequest) {
        return emailSettingsService.add(emailSettingsRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody EmailSettingsRequest emailSettingsRequest) {
        return emailSettingsService.edit(emailSettingsRequest);
    }

    @GetMapping("/list")
    public List<EmailSettingsResponse> list() {
        return emailSettingsService.list();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean delete(@PathVariable List<String> ids) {
        return emailSettingsService.delete(ids);
    }
}
