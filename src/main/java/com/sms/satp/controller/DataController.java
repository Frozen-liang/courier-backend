package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.DataImportDto;
import com.sms.satp.service.ApiInterfaceService;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.DATA_PATH)
public class DataController {

    private final ApiInterfaceService apiInterfaceService;

    public DataController(ApiInterfaceService apiInterfaceService) {
        this.apiInterfaceService = apiInterfaceService;
    }

    @PostMapping("/file")
    public Response importData(@RequestPart("file") MultipartFile file,
        @RequestParam("type") String type, @RequestParam("projectId") String projectId) throws IOException {
        apiInterfaceService.importByFile(file, type, projectId);
        return Response.ok().build();
    }

    @PostMapping("/url")
    public Response importByUrl(@Valid  @RequestBody DataImportDto dataImportDto) {
        apiInterfaceService.importByUrl(dataImportDto);
        return Response.ok().build();
    }
}