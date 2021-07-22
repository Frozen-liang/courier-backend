package com.sms.satp.service;

import com.sms.satp.dto.request.UserPasswordUpdateRequest;
import com.sms.satp.dto.request.UserQueryListRequest;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import java.util.List;

public interface UserService {

    UserResponse userProfile();

    UserResponse findById(String id);

    List<UserResponse> list(UserQueryListRequest request);

    Boolean add(UserRequest userRequest);

    Boolean edit(UserRequest userRequest);

    Boolean lock(List<String> ids);

    Boolean unlock(List<String> ids);

    Boolean updatePassword(UserPasswordUpdateRequest userPasswordUpdateRequest);
}