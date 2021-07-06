package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.API_PATH;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ApiImportRequest;
import com.sms.satp.dto.request.ApiPageRequest;
import com.sms.satp.dto.request.ApiRequest;
import com.sms.satp.dto.response.ApiResponse;
import com.sms.satp.service.ApiService;
import java.util.List;
import org.springframework.data.domain.Page;
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
    public Boolean importDocumentByFile(@Validated ApiImportRequest apiImportRequest) {
        return apiService.importDocumentByFile(apiImportRequest);
    }

    @PostMapping("/import")
    public Boolean importDocumentByProImpSourceIds(@RequestBody List<String> proImpSourceIds) {
        return apiService.importDocumentByProImpSourceIds(proImpSourceIds);
    }

    @GetMapping("{id}")
    public ApiResponse getById(@PathVariable("id") String id) {
        return apiService.findById(id);
    }

    @GetMapping("/page")
    public Page<ApiResponse> page(@Validated ApiPageRequest apiPageRequest) {
        return apiService.page(apiPageRequest);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiRequest apiRequest) {
        return apiService.add(apiRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiRequest apiRequest) {
        return apiService.edit(apiRequest);
    }

    @DeleteMapping("{ids}")
    public Boolean delete(@PathVariable("ids") List<String> ids) {
        return apiService.delete(ids);
    }

    @DeleteMapping("/delete/{ids}")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return apiService.deleteByIds(ids);
    }

    @DeleteMapping("/deleteAll")
    public Boolean deleteAll() {
        return apiService.deleteAll();
    }

    @PutMapping("/recover")
    public Boolean recover(@RequestBody List<String> ids) {
        return apiService.recover(ids);
    }

}
