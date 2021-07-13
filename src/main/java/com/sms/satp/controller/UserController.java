package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.USER;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import com.sms.satp.service.UserService;
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
@RequestMapping(USER)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserResponse userProfile() {
        return userService.userProfile();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole(@role.ADMIN)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody UserRequest userRequest) {
        return userService.add(userRequest);
    }

    @PutMapping
    @PreAuthorize("hasRole(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody UserRequest userRequest) {
        return userService.edit(userRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole(@role.ADMIN)")
    public List<UserResponse> list(String username, String groupId) {
        return userService.list(username, groupId);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return userService.delete(ids);
    }
}