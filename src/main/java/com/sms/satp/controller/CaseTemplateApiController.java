package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.CaseTemplateApiResponse;
import com.sms.satp.dto.UpdateCaseTemplateApiRequest;
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
    public Response batchAdd(@Valid @RequestBody BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest) {
        caseTemplateApiService.batchAdd(addCaseTemplateApiRequest);
        return Response.ok().build();
    }

    @DeleteMapping(value = "/{ids}")
    public Response deleteByIds(@PathVariable List<String> ids) {
        caseTemplateApiService.deleteByIds(ids);
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest) {
        caseTemplateApiService.edit(updateCaseTemplateApiRequest);
        return Response.ok().build();
    }

    @GetMapping(value = "/list/{caseTemplateId}/{remove}")
    public Response<List<CaseTemplateApiResponse>> listByCaseTemplateId(@PathVariable String caseTemplateId,
        @PathVariable boolean remove) {
        return Response.ok(caseTemplateApiService.listByCaseTemplateId(caseTemplateId, remove));
    }

    @GetMapping(value = "/{id}")
    public Response<CaseTemplateApiResponse> getSceneCaseApiById(@PathVariable String id) {
        return Response.ok(caseTemplateApiService.getSceneCaseApiById(id));
    }

}
