package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.TestCaseDto;
import com.sms.satp.service.TestCaseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.TEST_CASE_PATH)
public class TestCaseController {

    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping("/{caseId}")
    public Response<TestCaseDto> getTestCaseById(@PathVariable String caseId) {
        return Response.ok(testCaseService.getDtoById(caseId));
    }

    @PostMapping()
    public Response add(TestCaseDto testCaseDto) {
        testCaseService.add(testCaseDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(TestCaseDto testCaseDto) {
        testCaseService.edit(testCaseDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response deleteByIds(@PathVariable String[] ids) {
        for (String id : ids) {
            testCaseService.deleteById(id);
        }
        return Response.ok().build();
    }

}