package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.service.CaseTemplateGroupService;
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
@RequestMapping(Constants.CASE_TEMPLATE_GROUP_PATH)
public class CaseTemplateGroupController {

    private final CaseTemplateGroupService caseTemplateGroupService;

    public CaseTemplateGroupController(CaseTemplateGroupService caseTemplateGroupService) {
        this.caseTemplateGroupService = caseTemplateGroupService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_GROUP_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody CaseTemplateGroupRequest request) {
        return caseTemplateGroupService.add(request);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_GROUP_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody CaseTemplateGroupRequest request) {
        return caseTemplateGroupService.edit(request);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_GROUP_CRE_UPD_DEL)")
    public Boolean deleteById(@PathVariable String id) {
        return caseTemplateGroupService.deleteById(id);
    }

    @GetMapping(value = "/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_GROUP_QUERY_ALL)")
    public List<TreeResponse> getList(@PathVariable String projectId) {
        return caseTemplateGroupService.list(projectId);
    }
}
