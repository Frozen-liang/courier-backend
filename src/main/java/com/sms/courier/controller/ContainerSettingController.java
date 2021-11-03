package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.CONTAINER_SETTING_PATE;

import com.sms.courier.docker.service.DockerService;
import com.sms.courier.dto.request.ContainerSettingRequest;
import com.sms.courier.dto.response.ContainerSettingResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CONTAINER_SETTING_PATE)
public class ContainerSettingController {

    private final DockerService dockerService;

    public ContainerSettingController(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@RequestBody @Validated ContainerSettingRequest request) {
        return dockerService.editContainerSetting(request);
    }

    @GetMapping("/findOne")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public ContainerSettingResponse findOne() {
        return dockerService.findOne();
    }

}
