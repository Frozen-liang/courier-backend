package com.sms.courier.controller;


import static com.sms.courier.common.constant.Constants.OAUTH_SETTING;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.OAuthSettingRequest;
import com.sms.courier.dto.response.OAuthSettingResponse;
import com.sms.courier.service.OAuthSettingService;
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
@RequestMapping(OAUTH_SETTING)
@PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
public class OAuthSettingController {

    private final OAuthSettingService oauthSettingService;

    public OAuthSettingController(OAuthSettingService oauthSettingService) {
        this.oauthSettingService = oauthSettingService;
    }

    @GetMapping("/{id}")
    public OAuthSettingResponse getById(@PathVariable("id") String id) {
        return oauthSettingService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody OAuthSettingRequest authSettingRequest) {
        return oauthSettingService.add(authSettingRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody OAuthSettingRequest authSettingRequest) {
        return oauthSettingService.edit(authSettingRequest);
    }

    @GetMapping("/list")
    public List<OAuthSettingResponse> list() {
        return oauthSettingService.list();
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable String id) {
        return oauthSettingService.delete(id);
    }

}
