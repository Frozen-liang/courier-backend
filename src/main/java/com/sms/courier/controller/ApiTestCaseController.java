package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ApiTestCasePageRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateCaseByApiRequest;
import com.sms.courier.dto.response.ApiTestCasePageResponse;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.service.ApiTestCaseService;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTestCaseRequest apiTestCaseRequest) {
        return apiTestCaseService.add(apiTestCaseRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTestCaseRequest apiTestCaseRequest) {
        return apiTestCaseService.edit(apiTestCaseRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_QUERY_ALL)")
    public List<ApiTestCaseResponse> list(@RequestParam("apiId") ObjectId apiId,
        @RequestParam("projectId") ObjectId projectId, boolean removed) {
        return apiTestCaseService.list(apiId, projectId, removed);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTestCaseService.delete(ids);
    }

    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return apiTestCaseService.deleteByIds(ids);
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean deleteAll() {
        return apiTestCaseService.deleteAll();
    }

    @PutMapping("/recover")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean recover(@RequestBody List<String> ids) {
        return apiTestCaseService.recover(ids);
    }

    @PostMapping("/page")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_QUERY_ALL)")
    public Page<ApiTestCasePageResponse> page(@RequestBody ApiTestCasePageRequest request) {
        return apiTestCaseService.page(request);
    }

    @PostMapping("/updateCaseByApi")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean updateCaseByApi(@RequestBody List<UpdateCaseByApiRequest> requests) {
        return apiTestCaseService.updateCaseByApi(requests);
    }

    @PutMapping("/sync-api")
    @PreAuthorize("hasRoleOrAdmin(@role.CASE_CRE_UPD_DEL)")
    public Boolean syncApi(@Validated @RequestBody SyncApiRequest request) {
        return apiTestCaseService.syncApi(request);
    }

}