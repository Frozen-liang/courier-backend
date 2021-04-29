package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.service.CaseTemplateConnService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Response deleteById(@PathVariable String id) {
        caseTemplateConnService.deleteById(id);
        return Response.ok().build();
    }

}
