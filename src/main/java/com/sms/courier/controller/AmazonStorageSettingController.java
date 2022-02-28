package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.AMAZON_STORAGE_SETTING_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.AmazonStorageSettingRequest;
import com.sms.courier.dto.response.AmazonStorageSettingResponse;
import com.sms.courier.service.AmazonStorageSettingService;
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
@RequestMapping(AMAZON_STORAGE_SETTING_PATH)
public class AmazonStorageSettingController {
    private final AmazonStorageSettingService amazonStorageSettingService;

    public AmazonStorageSettingController(AmazonStorageSettingService aws3SettingService) {
        this.amazonStorageSettingService = aws3SettingService;
    }

    @GetMapping("/findOne")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public AmazonStorageSettingResponse findOne() {
        return amazonStorageSettingService.findOne();
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody AmazonStorageSettingRequest settingRequest) {
        return amazonStorageSettingService.add(settingRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody AmazonStorageSettingRequest settingRequest) {
        return amazonStorageSettingService.edit(settingRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean delete(@PathVariable String id) {
        return amazonStorageSettingService.delete(id);
    }

}
