package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.API_PATH;

import com.sms.satp.common.response.Response;
import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.dto.ApiPageRequest;
import com.sms.satp.dto.ApiRequest;
import com.sms.satp.dto.ApiResponse;
import com.sms.satp.service.ApiService;
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

    @PostMapping("/import")
    public Response<Boolean> importDocument(ApiImportRequest apiImportRequest) {
        return Response.ok(apiService.importDocument(apiImportRequest));
    }

    @GetMapping("{id}")
    public Response<ApiResponse> getById(@PathVariable("id") String id) {
        return Response.ok(apiService.findById(id));
    }

    @GetMapping("/page")
    public Response<Page<ApiResponse>> page(@Validated ApiPageRequest apiPageRequest) {
        return Response.ok(apiService.page(apiPageRequest));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody ApiRequest apiRequest) {
        return Response.ok(apiService.add(apiRequest));
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody ApiRequest apiRequest) {
        return Response.ok(apiService.edit(apiRequest));
    }

    @DeleteMapping("{ids}")
    public Response<Boolean> delete(@PathVariable("ids") String[] ids) {
        return Response.ok(apiService.delete(ids));
    }
    
}
