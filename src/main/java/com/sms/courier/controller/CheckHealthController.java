package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.CHECK_HEALTH;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckHealthController {

    @GetMapping(CHECK_HEALTH)
    public String checkHealth() {
        return "ok";
    }

}
