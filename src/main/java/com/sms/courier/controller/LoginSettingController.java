package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.LOGIN_SETTING_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.LoginSettingRequest;
import com.sms.courier.dto.response.LoginSettingResponse;
import com.sms.courier.service.LoginSettingService;
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
@RequestMapping(LOGIN_SETTING_PATH)
public class LoginSettingController {

    private final LoginSettingService loginSettingService;

    public LoginSettingController(LoginSettingService loginSettingService) {
        this.loginSettingService = loginSettingService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public LoginSettingResponse getById(@PathVariable("id") String id) {
        return loginSettingService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody LoginSettingRequest loginSettingRequest) {
        return loginSettingService.add(loginSettingRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody LoginSettingRequest loginSettingRequest) {
        return loginSettingService.edit(loginSettingRequest);
    }

    @GetMapping("/list")
    public List<LoginSettingResponse> list() {
        return loginSettingService.list();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean delete(@PathVariable List<String> ids) {
        return loginSettingService.delete(ids);
    }
}
