package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.ENGINE_PATH;

import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.EngineResponse;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.model.EngineAddress;
import com.sms.courier.engine.request.EngineMemberRequest;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ENGINE_PATH)
public class EngineController {

    private final EngineMemberManagement engineMemberManagement;

    public EngineController(EngineMemberManagement engineMemberManagement) {
        this.engineMemberManagement = engineMemberManagement;
    }

    @GetMapping("/get")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public List<EngineResponse> getRunningEngine() {
        return engineMemberManagement.getRunningEngine();
    }

    @GetMapping("/getAvailableEngine")
    public List<EngineAddress> getAvailableEngine() {
        return engineMemberManagement.getAvailableEngine();
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated @RequestBody EngineMemberRequest request) {
        return engineMemberManagement.edit(request);
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

    @DeleteMapping("/batchDelete/{names}")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean batchDeleteEngine(@PathVariable List<String> names) {
        return engineMemberManagement.batchDeleteEngine(names);
    }

    @GetMapping("/queryLog")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean queryLog(@Validated DockerLogRequest request) {
        return engineMemberManagement.queryLog(request);
    }

}
