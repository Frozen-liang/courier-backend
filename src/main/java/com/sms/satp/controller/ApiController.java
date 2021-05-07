package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.API_PATH;

import com.sms.satp.common.response.Response;
import com.sms.satp.dto.ApiImportRequest;
import com.sms.satp.service.ApiService;
import org.springframework.web.bind.annotation.PostMapping;
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
}
