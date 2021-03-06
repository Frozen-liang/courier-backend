package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.courier.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.service.CaseTemplateApiService;
import java.util.List;
import javax.validation.Valid;
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
@RequestMapping(Constants.CASE_TEMPLATE_API_PATH)
public class CaseTemplateApiController {

    private final CaseTemplateApiService caseTemplateApiService;

    public CaseTemplateApiController(CaseTemplateApiService caseTemplateApiService) {
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @PostMapping(value = "/batch")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_API_CRE_UPD_DEL)")
    public Boolean batchAdd(@Valid @RequestBody BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        return caseTemplateApiService.batchAdd(addCaseTemplateApiRequest);
    }

    @DeleteMapping(value = "/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_API_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return caseTemplateApiService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_API_CRE_UPD_DEL)")
    public Boolean edit(@Valid @RequestBody UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        return caseTemplateApiService.edit(updateCaseTemplateApiRequest);
    }

    @PutMapping("/batch")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_API_CRE_UPD_DEL)")
    public Boolean batchEdit(@Valid @RequestBody BatchUpdateCaseTemplateApiRequest batchUpdateCaseTemplateApiRequest) {
        return caseTemplateApiService.batchEdit(batchUpdateCaseTemplateApiRequest);
    }

    @GetMapping(value = "/{id}")
    public CaseTemplateApiResponse getCaseTemplateApiById(@PathVariable String id) {
        return caseTemplateApiService.getCaseTemplateApiById(id);
    }

    @PutMapping("/sync-api")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_API_CRE_UPD_DEL)")
    public Boolean syncApi(@Validated @RequestBody SyncApiRequest request) {
        return caseTemplateApiService.syncApi(request);
    }
}
