package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.ENGINE_PATH;

import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineRegistrationResponse;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.request.EngineRegistrationRequest;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        CustomUser engine = CustomUser.createEngine(destination);
        return EngineRegistrationResponse.builder().subscribeAddress(destination)
            .token(jwtTokenManager.generateAccessToken(engine))
            .build();
    }

    @PutMapping("/open")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean openEngine(String id) {
        return engineMemberManagement.openEngine(id);
    }

    @PutMapping("/close")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean closeEngine(String id) {
        return engineMemberManagement.closeEngine(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean createEngine() {
        return engineMemberManagement.createEngine();
    }

    @PostMapping("/restart")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean restartEngine(String name) {
        return engineMemberManagement.restartEngine(name);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean deleteEngine(String name) {
        return engineMemberManagement.deleteEngine(name);
    }

    @GetMapping("/queryLog")
    public Boolean queryLog(@Validated DockerLogRequest request) {
        return engineMemberManagement.queryLog(request);
    }

}
