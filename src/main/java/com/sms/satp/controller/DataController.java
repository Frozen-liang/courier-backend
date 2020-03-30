package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.DocumentImportDto;
import com.sms.satp.entity.dto.ImportWay;
import com.sms.satp.service.ApiInterfaceService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.DATA_PATH)
public class DataController {

    private final ApiInterfaceService apiInterfaceService;

    public DataController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    @PostMapping("/file")
    public Response importData(@Valid DocumentImportDto documentImportDto) {
        apiInterfaceService.importDocument(documentImportDto, ImportWay.FILE);
        return Response.ok().build();
    }

    @PostMapping("/url")
    public Response importByUrl(@Valid @RequestBody DocumentImportDto documentImportDto) {
        apiInterfaceService.importDocument(documentImportDto, ImportWay.URL);
        return Response.ok().build();
    }
}