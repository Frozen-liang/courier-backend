package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.ROLE;

import com.sms.courier.dto.response.RoleResponse;
import com.sms.courier.service.RoleService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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
