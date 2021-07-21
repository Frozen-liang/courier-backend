package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SCENE_CASE_API_PATH)
public class SceneCaseApiController {

    private final SceneCaseApiService sceneCaseApiService;

    public SceneCaseApiController(SceneCaseApiService sceneCaseApiService) {
        this.sceneCaseApiService = sceneCaseApiService;
    }

    @PostMapping(value = "/batch")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_API_CRE_UPD_DEL)")
    public Boolean batchAdd(@Valid @RequestBody BatchAddSceneCaseApiRequest addSceneCaseApiDto) {
        return sceneCaseApiService.batchAdd(addSceneCaseApiDto);
    }

    @DeleteMapping(value = "/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_API_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return sceneCaseApiService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_API_CRE_UPD_DEL)")
    public Boolean edit(@Valid @RequestBody UpdateSceneCaseApiRequest updateSceneCaseApiRequest) {
        return sceneCaseApiService.edit(updateSceneCaseApiRequest);
    }

    @GetMapping(value = "/{id}")
    public SceneCaseApiResponse getSceneCaseApiById(@PathVariable String id) {
        return sceneCaseApiService.getSceneCaseApiById(id);
    }

}
