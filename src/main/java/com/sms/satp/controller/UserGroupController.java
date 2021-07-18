package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.USER_GROUP;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.UserGroupRequest;
import com.sms.satp.dto.response.UserGroupResponse;
import com.sms.satp.service.UserGroupService;
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
@RequestMapping(USER_GROUP)
public class UserGroupController {

    private final UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.USER_GROUP_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody UserGroupRequest userGroupRequest) {
        return userGroupService.add(userGroupRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.USER_GROUP_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody UserGroupRequest userGroupRequest) {
        return userGroupService.edit(userGroupRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.USER_GROUP_QUERY_ALL)")
    public List<UserGroupResponse> list() {
        return userGroupService.list();
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.USER_GROUP_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return userGroupService.delete(ids);
    }
}