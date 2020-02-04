package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.ApiInterfaceDto;
import com.sms.satp.service.ApiInterfaceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.INTERFACE_PATH)
public class ApiInterfaceController {

    private final ApiInterfaceService apiInterfaceService;

    public ApiInterfaceController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    @GetMapping("/list")
    public Response<List<ApiInterfaceDto>> list() {
        return null;
    }

    @GetMapping("/{id}")
    public Response<ApiInterfaceDto> getInfoById(@PathVariable String id) {
        return null;
    }

}