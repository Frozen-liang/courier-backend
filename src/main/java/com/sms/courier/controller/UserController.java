package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.USER_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.UserPasswordUpdateRequest;
import com.sms.courier.dto.request.UserQueryListRequest;
import com.sms.courier.dto.request.UserRequest;
import com.sms.courier.dto.response.UserProfileResponse;
import com.sms.courier.dto.response.UserResponse;
import com.sms.courier.service.UserService;
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
@RequestMapping(USER_PATH)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileResponse userProfile() {
        return userService.userProfile();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.USER_CREATE)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody UserRequest userRequest) {
        return userService.add(userRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.USER_UPDATE)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody UserRequest userRequest) {
        return userService.edit(userRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.USER_QUERY_ALL)")
    public List<UserResponse> list(UserQueryListRequest request) {
        return userService.list(request);
    }

    @DeleteMapping("/lock/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.USER_STATUS_CONTROL)")
    public Boolean lock(@PathVariable List<String> ids) {
        return userService.lock(ids);
    }

    @DeleteMapping("/unlock/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.USER_STATUS_CONTROL)")
    public Boolean unlock(@PathVariable List<String> ids) {
        return userService.unlock(ids);
    }

    @PutMapping("/update/password")
    public Boolean updatePassword(@Validated @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {
        return userService.updatePassword(userPasswordUpdateRequest);
    }
}