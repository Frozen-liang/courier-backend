package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.ROLE;

import com.sms.satp.dto.response.RoleResponse;
import com.sms.satp.service.RoleService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ROLE)
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/groupId")
    public List<RoleResponse> findRolesByGroupById(String groupId) {
        return roleService.findRolesByGroupId(groupId);
    }

}
