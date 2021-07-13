package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.service.CaseTemplateApiService;
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
@RequestMapping(Constants.CASE_TEMPLATE_API_PATH)
public class CaseTemplateApiController {

    private final CaseTemplateApiService caseTemplateApiService;

    public CaseTemplateApiController(CaseTemplateApiService caseTemplateApiService) {
        this.caseTemplateApiService = caseTemplateApiService;
    }

    @PostMapping(value = "/batch")
    public Boolean batchAdd(@Valid @RequestBody BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        return caseTemplateApiService.batchAdd(addCaseTemplateApiRequest);
    }

    @DeleteMapping(value = "/{ids}")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return caseTemplateApiService.deleteByIds(ids);
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        return caseTemplateApiService.edit(updateCaseTemplateApiRequest);
    }

    @PutMapping("/batch")
    public Boolean batchEdit(@Valid @RequestBody BatchUpdateCaseTemplateApiRequest batchUpdateCaseTemplateApiRequest) {
        return caseTemplateApiService.batchEdit(batchUpdateCaseTemplateApiRequest);
    }

    @GetMapping(value = "/{id}")
    public CaseTemplateApiResponse getCaseTemplateApiById(@PathVariable String id) {
        return caseTemplateApiService.getCaseTemplateApiById(id);
    }

}
