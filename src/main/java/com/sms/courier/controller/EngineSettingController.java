package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.ENGINE_SETTING_PATE;

import com.sms.courier.dto.request.EngineSettingRequest;
import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.EngineSettingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENGINE_SETTING_PATE)
public class EngineSettingController {

    private final EngineSettingService engineSettingService;

    public EngineSettingController(EngineSettingService engineSettingService) {
        this.engineSettingService = engineSettingService;
    }

    @PutMapping
    public Boolean edit(@RequestBody @Validated EngineSettingRequest request) {
        return engineSettingService.edit(request);
    }

    @GetMapping("/findOne")
    public EngineSettingResponse findOne() {
        return engineSettingService.findOne();
    }

}
