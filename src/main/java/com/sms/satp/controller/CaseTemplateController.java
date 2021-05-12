package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.AddCaseTemplateRequest;
import com.sms.satp.dto.CaseTemplateResponse;
import com.sms.satp.dto.CaseTemplateSearchDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.UpdateCaseTemplateRequest;
import com.sms.satp.service.CaseTemplateService;
import java.util.List;
import javax.validation.Valid;
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
    public Response add(@Valid @RequestBody AddCaseTemplateRequest addCaseTemplateRequest) {
        caseTemplateService.add(addCaseTemplateRequest);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response deleteByIds(@PathVariable List<String> ids) {
        caseTemplateService.deleteByIds(ids);
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        caseTemplateService.edit(updateCaseTemplateRequest);
        return Response.ok().build();
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<CaseTemplateResponse>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(caseTemplateService.page(pageDto, projectId));
    }

    @GetMapping("/search/{projectId}")
    public Response<Page<CaseTemplateResponse>> search(CaseTemplateSearchDto searchDto,
        @PathVariable String projectId) {
        return Response.ok(caseTemplateService.search(searchDto, projectId));
    }

}
