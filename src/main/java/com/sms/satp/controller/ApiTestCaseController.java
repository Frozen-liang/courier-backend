package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ApiTestCaseExecuteRequest;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.service.ApiTestCaseService;
import java.util.List;
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
@RequestMapping(Constants.API_TEST_CASE_PATH)
public class ApiTestCaseController {

    private final ApiTestCaseService apiTestCaseService;

    public ApiTestCaseController(ApiTestCaseService apiTestCaseService) {
        this.apiTestCaseService = apiTestCaseService;
    }

    @GetMapping("/{id}")
    public ApiTestCaseResponse getById(@PathVariable("id") String id) {
        return apiTestCaseService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTestCaseRequest apiTestCaseRequest) {
        return apiTestCaseService.add(apiTestCaseRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTestCaseRequest apiTestCaseRequest) {
        return apiTestCaseService.edit(apiTestCaseRequest);
    }

    @GetMapping("/list")
    public List<ApiTestCaseResponse> list(String apiId, String projectId) {
        return apiTestCaseService.list(apiId, projectId);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTestCaseService.delete(ids);
    }

    @PostMapping("/execute")
    public Boolean execute(@Validated @RequestBody ApiTestCaseExecuteRequest apiTestCaseExecuteRequest) {
        return apiTestCaseService.execute(apiTestCaseExecuteRequest);
    }
}