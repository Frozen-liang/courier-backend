package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.USER_ENV_PATH;

import com.sms.courier.dto.request.UserEnvConnRequest;
import com.sms.courier.dto.response.UserEnvConnResponse;
import com.sms.courier.service.UserEnvConnService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USER_ENV_PATH)
public class UserEnvConnController {

    private final UserEnvConnService userEnvConnService;

    public UserEnvConnController(UserEnvConnService userEnvConnService) {
        this.userEnvConnService = userEnvConnService;
    }

    @GetMapping
    public UserEnvConnResponse userEnv(@RequestParam String projectId) {
        return userEnvConnService.userEnv(projectId);
    }

    @PostMapping("/conn")
    public UserEnvConnResponse userEnvConn(@RequestBody UserEnvConnRequest request) {
        return userEnvConnService.userEnvConn(request);
    }

}
