package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.MockSettingRequest;
import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.service.MockSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.MOCK_SETTING_PATH)
public class MockSettingController {

    private final MockSettingService mockSettingService;

    public MockSettingController(MockSettingService mockSettingService) {
        this.mockSettingService = mockSettingService;
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody MockSettingRequest mockSettingRequest) {
        return mockSettingService.edit(mockSettingRequest);
    }

    @GetMapping("/findOne")
    @PreAuthorize("hasRoleOrAdmin(@role.MOCK_SETTING_QUERY_ALL)")
    public MockSettingResponse findOne() {
        return mockSettingService.findOne();
    }

    @PutMapping("/reset-token/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean resetToken(@PathVariable String id) {
        return mockSettingService.resetToken(id);
    }

}
