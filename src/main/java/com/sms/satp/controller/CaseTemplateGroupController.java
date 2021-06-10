package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddCaseTemplateGroupRequest;
import com.sms.satp.dto.request.SearchCaseTemplateGroupRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.service.CaseTemplateGroupService;
import java.util.List;
import javax.validation.Valid;
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
    public Boolean add(@Valid @RequestBody AddCaseTemplateGroupRequest request) {
        return caseTemplateGroupService.add(request);
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody UpdateCaseTemplateGroupRequest request) {
        return caseTemplateGroupService.edit(request);
    }

    @DeleteMapping(value = "/{id}")
    public Boolean deleteById(@PathVariable String id) {
        return caseTemplateGroupService.deleteById(id);
    }

    @GetMapping
    public List<CaseTemplateGroupResponse> getList(SearchCaseTemplateGroupRequest request) {
        return caseTemplateGroupService.getList(request);
    }
}
