package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.CodeGenRequest;
import com.sms.courier.service.GeneratorService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.GENERATE_CODE)
public class GeneratorController {

    private final GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @SneakyThrows(IOException.class)
    @GetMapping("/download")
    public void downLoadCode(HttpServletResponse response, @Validated CodeGenRequest request) {
        generatorService.generator(response.getOutputStream(), request);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s-generated.zip\"",
            "code"));
    }

}
