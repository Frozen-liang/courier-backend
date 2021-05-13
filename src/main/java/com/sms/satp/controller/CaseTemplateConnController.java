package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.service.CaseTemplateConnService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.CASE_TEMPLATE_CONN_PATH)
public class CaseTemplateConnController {

    private final CaseTemplateConnService caseTemplateConnService;

    public CaseTemplateConnController(CaseTemplateConnService caseTemplateConnService) {
        this.caseTemplateConnService = caseTemplateConnService;
    }

    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable String id) {
        return caseTemplateConnService.deleteById(id);
    }

    @PostMapping
    public Boolean add(@Valid @RequestBody AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        return caseTemplateConnService.add(addCaseTemplateConnRequest);
    }
}
