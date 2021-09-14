package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.request.ConvertCaseTemplateRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateDetailResponse;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.response.IdResponse;
import com.sms.courier.service.CaseTemplateService;
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
@RequestMapping(Constants.CASE_TEMPLATE_PATH)
public class CaseTemplateController {

    private final CaseTemplateService caseTemplateService;

    public CaseTemplateController(CaseTemplateService caseTemplateService) {
        this.caseTemplateService = caseTemplateService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean add(@Valid @RequestBody AddCaseTemplateRequest addCaseTemplateRequest) {
        return caseTemplateService.add(addCaseTemplateRequest);
    }

    @PostMapping("/convert")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public IdResponse convert(@Valid @RequestBody ConvertCaseTemplateRequest convertCaseTemplateRequest) {
        return caseTemplateService.add(convertCaseTemplateRequest);
    }

    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return caseTemplateService.deleteByIds(ids);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean edit(@Valid @RequestBody UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        return caseTemplateService.edit(updateCaseTemplateRequest);
    }

    @PostMapping("/page/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_QUERY_ALL)")
    public Page<CaseTemplateResponse> page(@RequestBody CaseTemplateSearchRequest searchDto,
        @PathVariable ObjectId projectId) {
        return caseTemplateService.page(searchDto, projectId);
    }

    @GetMapping("/list/{caseTemplateId}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_QUERY_ALL)")
    public CaseTemplateDetailResponse getApiList(@PathVariable String caseTemplateId) {
        return caseTemplateService.getApiList(caseTemplateId);
    }

    @PostMapping("/api")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean addApi(@Valid @RequestBody AddCaseTemplateApiByIdsRequest request) {
        return caseTemplateService.addApi(request);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return caseTemplateService.delete(ids);
    }

    @PutMapping("/recover")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_TEMPLATE_CRE_UPD_DEL)")
    public Boolean recover(@RequestBody List<String> ids) {
        return caseTemplateService.recover(ids);
    }
}
