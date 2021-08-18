package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.ENGINE_PATH;
import static com.sms.courier.security.TokenType.ENGINE;

import com.sms.courier.dto.response.EngineRegistrationResponse;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENGINE_PATH)
public class EngineController {

    private final EngineMemberManagement engineMemberManagement;
    private final JwtTokenManager jwtTokenManager;

    public EngineController(EngineMemberManagement engineMemberManagement,
        JwtTokenManager jwtTokenManager) {
        this.engineMemberManagement = engineMemberManagement;
        this.jwtTokenManager = jwtTokenManager;
    }

    @GetMapping("/get")
    public List<EngineResponse> getRunningEngine() {
        return engineMemberManagement.getRunningEngine();
    }

    @PostMapping("/bind")
    public EngineRegistrationResponse bind(@Validated @RequestBody EngineRegistrationRequest request) {
        String destination = engineMemberManagement.bind(request);
        CustomUser engine = new CustomUser("engine", "", Collections.emptyList(), destination, "",
            ENGINE, LocalDate.now());
        return EngineRegistrationResponse.builder().subscribeAddress(destination)
            .token(jwtTokenManager.generateAccessToken(engine))
            .build();
    }
}
