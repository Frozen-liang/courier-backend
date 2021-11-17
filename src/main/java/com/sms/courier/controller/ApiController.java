package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.API_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.service.ApiService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PATH)
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/import-by-file")
    @PreAuthorize("hasRoleOrAdmin(@role.API_IMPORT_BY_FILE)")
    public Boolean importDocumentByFile(@Validated ApiImportRequest apiImportRequest) {
        return apiService.importDocumentByFile(apiImportRequest);
    }

    @PostMapping("/sync")
    @PreAuthorize("hasRoleOrAdmin(@role.API_SYNC)")
    public Boolean syncApiByProImpSourceIds(@RequestBody List<String> proImpSourceIds) {
        return apiService.syncApiByProImpSourceIds(proImpSourceIds);
    }

    @GetMapping("{id}")
    public ApiResponse getById(@PathVariable("id") String id) {
        return apiService.findById(id);
    }

    @PostMapping("/page")
    @PreAuthorize("hasRoleOrAdmin(@role.API_QUERY_ALL)")
    public Page<ApiPageResponse> page(@RequestBody @Validated ApiPageRequest apiPageRequest) {
        return apiService.page(apiPageRequest);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiRequest apiRequest) {
        return apiService.add(apiRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiRequest apiRequest) {
        return apiService.edit(apiRequest);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable("ids") List<String> ids) {
        return apiService.delete(ids);
    }

    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return apiService.deleteByIds(ids);
    }

    @DeleteMapping("/deleteAll/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean deleteAll(@PathVariable String projectId) {
        return apiService.deleteAll(projectId);
    }

    @PutMapping("/recover")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean recover(@RequestBody List<String> ids) {
        return apiService.recover(ids);
    }

    @GetMapping("/count/pid/{projectId}")
    public Long count(@PathVariable String projectId) {
        return apiService.count(projectId);
    }

    @PutMapping("/batch/updateByIds")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean batchUpdateByIds(@RequestBody BatchUpdateByIdRequest<Object> batchUpdateRequest) {
        return apiService.batchUpdateByIds(batchUpdateRequest);
    }

    @GetMapping("/scene/count/pid/{projectId}")
    public Long sceneCount(@PathVariable ObjectId projectId) {
        return apiService.sceneCount(projectId);
    }

    @GetMapping("/case/count/pid/{projectId}")
    public Long caseCount(@PathVariable ObjectId projectId) {
        return apiService.caseCount(projectId);
    }

    @PutMapping("/reset/version/{historyId}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_CRE_UPD_DEL)")
    public Boolean resetApiVersion(@PathVariable String historyId) {
        return apiService.resetApiVersion(historyId);
    }

}