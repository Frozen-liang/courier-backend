package com.sms.satp.service;

import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import java.util.List;

public interface UserService {

    UserResponse userProfile();

    UserResponse findById(String id);

    List<UserResponse> list(String username, String groupId);

    Boolean add(UserRequest userRequest);

    Boolean edit(UserRequest userRequest);

    Boolean delete(List<String> ids);

}