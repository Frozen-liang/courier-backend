package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.service.ApiInterfaceService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.DATA_PATH)
public class DataController {

    private final ApiInterfaceService apiInterfaceService;

    public DataController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    @PostMapping()
    public void importData(@RequestParam("file") MultipartFile file, String type) {
    }
}