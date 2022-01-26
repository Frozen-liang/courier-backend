package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.OPEN_API_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.OpenApiSettingRequest;
import com.sms.courier.dto.response.OpenApiSettingResponse;
import com.sms.courier.service.OpenApiSettingService;
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
@RequestMapping(OPEN_API_PATH)
@PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
public class OpenApiSettingController {

    private final OpenApiSettingService openApiSettingService;

    public OpenApiSettingController(OpenApiSettingService openApiSettingService) {
        this.openApiSettingService = openApiSettingService;
    }

    @GetMapping("/{id}")
    public OpenApiSettingResponse getById(@PathVariable("id") String id) {
        return openApiSettingService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody OpenApiSettingRequest openApiSettingRequest) {
        return openApiSettingService.add(openApiSettingRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody OpenApiSettingRequest openApiSettingRequest) {
        return openApiSettingService.edit(openApiSettingRequest);
    }

    @GetMapping("/list")
    public List<OpenApiSettingResponse> list() {
        return openApiSettingService.list();
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return openApiSettingService.delete(ids);
    }

    @PutMapping("/enable/{ids}")
    public Boolean enable(@PathVariable List<String> ids) {
        return openApiSettingService.enable(ids);
    }

    @PutMapping("/unable/{ids}")
    public Boolean unable(@PathVariable List<String> ids) {
        return openApiSettingService.unable(ids);
    }
}
