package com.sms.courier.service.impl;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.dto.UserEntityAuthority;
import com.sms.courier.dto.request.UserPasswordUpdateRequest;
import com.sms.courier.dto.request.UserQueryListRequest;
import com.sms.courier.dto.request.UserRequest;
import com.sms.courier.dto.response.UserResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.entity.workspace.WorkspaceEntity;
import com.sms.courier.mapper.UserMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.UserGroupRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.repository.WorkspaceRepository;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.UserGroupService;
import com.sms.courier.service.UserService;
import com.sms.courier.utils.Assert;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupService userGroupService;
    private final WorkspaceRepository workspaceRepository;
    private final CommonRepository commonRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private static final String GROUP_ID = "groupId";

    public UserServiceImpl(UserRepository userRepository,
        UserGroupRepository userGroupRepository, UserGroupService userGroupService,
        WorkspaceRepository workspaceRepository,
        CommonRepository commonRepository,
        UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.userGroupService = userGroupService;
        this.workspaceRepository = workspaceRepository;
        this.commonRepository = commonRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse userProfile() {
        CustomUser currentUser = SecurityUtil.getCurrentUser();
        return userMapper.toUserResponse(currentUser);
    }

    @Override
    public UserResponse findById(String id) {
        return userRepository.findById(id).map(userMapper::toDto)
            .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.GET_USER_BY_ID_ERROR));
    }

    @Override
    public List<UserResponse> list(UserQueryListRequest request) {
        try {
            Sort sort = Sort.by(Direction.DESC, CommonField.CREATE_DATE_TIME.getName());
            UserEntity userEntity = userMapper.toEntity(request);
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(GROUP_ID, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnorePaths(CommonField.REMOVE.getName())
                .withIgnoreNullValues();
            Example<UserEntity> example = Example.of(userEntity, exampleMatcher);
            List<UserResponse> userResponseList = userMapper.toDtoList(userRepository.findAll(example, sort));
            List<String> userIds = Optional.ofNullable(request.getWorkspaceId())
                .map(workspaceRepository::findById)
                .map(Optional::get)

                .map(WorkspaceEntity::getUserIds)
                .orElse(Collections.emptyList());
            List<String> groupIds = userResponseList.stream().map(UserResponse::getGroupId)
                .collect(Collectors.toList());
            Map<String, String> groupMap = userGroupRepository.findAllByIdIn(groupIds)
                .collect(Collectors.toMap(UserGroupEntity::getId, UserGroupEntity::getName));
            userResponseList.forEach(user -> {
                user.setExist(userIds.contains(user.getId()));
                user.setGroupName(groupMap.get(user.getGroupId()));
            });
            return userResponseList;
        } catch (Exception e) {
            log.error("Failed to get the User list!", e);
            throw new ApiTestPlatformException(ErrorCode.GET_USER_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = OperationType.ADD, operationModule = OperationModule.USER,
        template = "{{#userRequest.username}}")
    public Boolean add(UserRequest userRequest) {
        log.info("UserService-add()-params: [User]={}", userRequest.toString());
        try {
            Assert.isTrue(checkPassword(userRequest.getPassword()),
                "Passwords must contain lowercase, uppercase, number and "
                    + "special characters.");
            Assert.isFalse(userRepository.existsByUsername(userRequest.getUsername()), "The username exists.");
            Assert.isFalse(userRepository.existsByEmail(userRequest.getEmail()), "The email exists.");
            UserEntity user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.insert(user);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(ErrorCode.ADD_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    private boolean checkPassword(String password) {
        Pattern pattern = Pattern
            .compile("^(?=.*\\d)(?=.*?[_\\-@&=!#$%^*<.>~`?'\"/;:'(){}\\[\\]])(?=.*[a-z])(?=.*[A-Z]).{8,40}$");
        return pattern.matcher(password).matches();
    }

    @Override
    @LogRecord(operationType = OperationType.EDIT, operationModule = OperationModule.USER,
        template = "{{#userRequest.username}}")
    public Boolean edit(UserRequest userRequest) {
        log.info("UserService-edit()-params: [User]={}", userRequest.toString());
        try {
            UserEntity oldUser = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(ErrorCode.EDIT_NOT_EXIST_ERROR, "User", userRequest.getId()));
            UserEntity user = userMapper.toEntity(userRequest);
            Assert.isFalse(!oldUser.getUsername().equals(userRequest.getUsername())
                && userRepository.existsByUsername(userRequest.getUsername()), "The username exists.");
            Assert.isFalse(!oldUser.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail()), "The email exists.");
            user.setPassword(oldUser.getPassword());
            user.setRemoved(oldUser.isRemoved());
            userRepository.save(user);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (DuplicateKeyException e) {
            log.error("The email:{} exist!", userRequest.getEmail());
            throw ExceptionUtils.mpe("The email:%s exist!", userRequest.getEmail());
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(ErrorCode.EDIT_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = OperationType.LOCK, operationModule = OperationModule.USER,
        template = "{{#result?.![#this.username]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean lock(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, UserEntity.class);
        } catch (Exception e) {
            log.error("Failed to lock the User!", e);
            throw new ApiTestPlatformException(ErrorCode.LOCK_USER_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = OperationType.UNLOCK, operationModule = OperationModule.USER,
        template = "{{#result?.![#this.username]}}", enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean unlock(List<String> ids) {
        try {
            return commonRepository.recover(ids, UserEntity.class);
        } catch (Exception e) {
            log.error("Failed to unlock the User!", e);
            throw new ApiTestPlatformException(ErrorCode.UNLOCK_USER_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean updatePassword(UserPasswordUpdateRequest request) {
        try {
            Assert.isTrue(StringUtils.equals(request.getNewPassword(), request.getConfirmPassword()),
                "The two passwords don't match.");
            Assert.isTrue(checkPassword(request.getNewPassword()),
                "Passwords must contain lowercase, uppercase, number and "
                    + "special characters.");
            UserEntity userEntity = userRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.mpe("The user not exists."));
            Assert.isTrue(passwordEncoder.matches(request.getOldPassword(), userEntity.getPassword()),
                "The old password is not correct.");
            userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error("id:{},message:{}", request.getId(), e.getMessage());
            throw e;
        }
    }

    @Override
    public UserEntityAuthority getUserDetailsByUsernameOrEmail(String username) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() ->
            new UsernameNotFoundException(String.format("The user %s was not found.", username)));
        return UserEntityAuthority.builder()
            .userEntity(userEntity)
            .authorities(userGroupService.getAuthoritiesByUserGroup(userEntity.getGroupId()))
            .build();
    }

    @Override
    public UserEntityAuthority getUserDetailsByUserId(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
            new UsernameNotFoundException(String.format("The userId %s was not found.", id)));
        return UserEntityAuthority.builder()
            .userEntity(userEntity)
            .authorities(userGroupService.getAuthoritiesByUserGroup(userEntity.getGroupId()))
            .build();
    }

}