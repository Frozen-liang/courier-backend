package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.ConvertCaseTemplateRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateDetailResponse;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.dto.response.IdResponse;
import com.sms.satp.service.CaseTemplateService;
import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
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
    public Boolean add(@Valid @RequestBody AddCaseTemplateRequest addCaseTemplateRequest) {
        return caseTemplateService.add(addCaseTemplateRequest);
    }

    @PostMapping("/convert")
    public IdResponse convert(@Valid @RequestBody ConvertCaseTemplateRequest convertCaseTemplateRequest) {
        return caseTemplateService.add(convertCaseTemplateRequest);
    }

    @DeleteMapping("/delete/{ids}")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return caseTemplateService.deleteByIds(ids);
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        return caseTemplateService.edit(updateCaseTemplateRequest);
    }

    @PostMapping("/page/{projectId}")
    public Page<CaseTemplateResponse> page(@RequestBody CaseTemplateSearchRequest searchDto,
        @PathVariable ObjectId projectId) {
        return caseTemplateService.page(searchDto, projectId);
    }

    @GetMapping("/list/{caseTemplateId}")
    public CaseTemplateDetailResponse getApiList(@PathVariable String caseTemplateId) {
        return caseTemplateService.getApiList(caseTemplateId);
    }

    @PostMapping("/api")
    public Boolean addApi(@Valid @RequestBody AddCaseTemplateApiByIdsRequest request) {
        return caseTemplateService.addApi(request);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return caseTemplateService.delete(ids);
    }

    @PutMapping("/recover")
    public Boolean recover(@RequestBody List<String> ids) {
        return caseTemplateService.recover(ids);
    }
}
