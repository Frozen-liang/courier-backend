package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddCaseTemplateConnRequest;
import com.sms.courier.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.response.SceneTemplateResponse;
import com.sms.courier.service.SceneCaseService;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
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
@RequestMapping(Constants.SCENE_CASE_PATH)
public class SceneCaseController {

    private final SceneCaseService sceneCaseService;

    public SceneCaseController(SceneCaseService sceneCaseService) {
        this.sceneCaseService = sceneCaseService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean add(@Valid @RequestBody AddSceneCaseRequest sceneCaseDto) {
        return sceneCaseService.add(sceneCaseDto);
    }

    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return sceneCaseService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean edit(@Valid @RequestBody UpdateSceneCaseRequest sceneCaseDto) {
        return sceneCaseService.edit(sceneCaseDto);
    }

    @PostMapping("/page/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_QUERY_ALL)")
    public Page<SceneCaseResponse> page(@RequestBody SearchSceneCaseRequest searchDto,
        @PathVariable ObjectId projectId) {
        return sceneCaseService.page(searchDto, projectId);
    }

    @GetMapping("/conn/{id}")
    public SceneTemplateResponse getConn(@PathVariable String id) {
        return sceneCaseService.getConn(id);
    }

    @PutMapping("/conn/edit")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean editConn(@RequestBody UpdateSceneCaseConnRequest updateSceneTemplateRequest) {
        return sceneCaseService.editConn(updateSceneTemplateRequest);
    }

    @PostMapping("/api")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean addApi(@Valid @RequestBody AddSceneCaseApiByIdsRequest request) {
        return sceneCaseService.addApi(request);
    }

    @PostMapping("/template")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean addTemplate(@Valid @RequestBody AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        return sceneCaseService.addTemplate(addCaseTemplateConnRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return sceneCaseService.delete(ids);
    }

    @PutMapping("/recover")
    @PreAuthorize("hasRoleOrAdmin(@role.SCENE_CASE_CRE_UPD_DEL)")
    public Boolean recover(@RequestBody List<String> ids) {
        return sceneCaseService.recover(ids);
    }
}
