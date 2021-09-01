package com.sms.courier.service;

import com.sms.courier.dto.UserEntityAuthority;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.request.UserPasswordUpdateRequest;
import com.sms.courier.dto.request.UserQueryListRequest;
import com.sms.courier.dto.request.UserRequest;
import com.sms.courier.dto.response.UserInfoResponse;
import com.sms.courier.dto.response.UserProfileResponse;
import com.sms.courier.dto.response.UserResponse;
import com.sms.courier.entity.system.UserEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;

public interface UserService {

    UserProfileResponse userProfile();

    UserResponse findById(String id);

    List<UserResponse> list(UserQueryListRequest request);

    Boolean add(UserRequest userRequest);

    Boolean edit(UserRequest userRequest);

    Boolean lock(List<String> ids);

    Boolean unlock(List<String> ids);

    Boolean updatePassword(UserPasswordUpdateRequest userPasswordUpdateRequest);

    UserEntityAuthority getUserDetailsByUsernameOrEmail(String username);

    UserEntityAuthority getUserDetailsByUserId(String id);

    Boolean batchUpdateByIds(BatchUpdateByIdRequest<ObjectId> batchUpdateRequest);

    Optional<UserEntity> findByEmail(String email);

    Boolean setPasswordByEmail(String email, String password);
    Boolean batchUpdateByIds(BatchUpdateByIdRequest<Object> batchUpdateRequest);

    List<UserInfoResponse> getByWorkspaceId(String workspaceId);
}