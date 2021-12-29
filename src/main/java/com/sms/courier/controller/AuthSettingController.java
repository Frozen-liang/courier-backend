package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.AUTH_SETTING;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.AuthSettingRequest;
import com.sms.courier.dto.response.AuthSettingResponse;
import com.sms.courier.service.AuthSettingService;
import java.util.List;
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
@RequestMapping(AUTH_SETTING)
public class AuthSettingController {

    private final AuthSettingService authSettingService;

    public AuthSettingController(AuthSettingService authSettingService) {
        this.authSettingService = authSettingService;
    }

    @GetMapping("/{id}")
    public AuthSettingResponse getById(@PathVariable("id") String id) {
        return authSettingService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody AuthSettingRequest authSettingRequest) {
        return authSettingService.add(authSettingRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody AuthSettingRequest authSettingRequest) {
        return authSettingService.edit(authSettingRequest);
    }

    @GetMapping("/list")
    public List<AuthSettingResponse> list() {
        return authSettingService.list();
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable String id) {
        return authSettingService.delete(id);
    }
}
