package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.USER;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.LOCK;
import static com.sms.satp.common.enums.OperationType.UNLOCK;
import static com.sms.satp.common.exception.ErrorCode.ADD_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.LOCK_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.UNLOCK_USER_BY_ID_ERROR;
import static com.sms.satp.common.field.CommonField.CREATE_DATE_TIME;
import static com.sms.satp.common.field.CommonField.REMOVE;
import static com.sms.satp.utils.Assert.isFalse;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.UserEntityAuthority;
import com.sms.satp.dto.request.UserPasswordUpdateRequest;
import com.sms.satp.dto.request.UserQueryListRequest;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.entity.workspace.WorkspaceEntity;
import com.sms.satp.mapper.UserMapper;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.UserGroupRepository;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.repository.WorkspaceRepository;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.UserGroupService;
import com.sms.satp.service.UserService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.SecurityUtil;
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
            .orElseThrow(() -> ExceptionUtils.mpe(GET_USER_BY_ID_ERROR));
    }

    @Override
    public List<UserResponse> list(UserQueryListRequest request) {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getName());
            UserEntity userEntity = userMapper.toEntity(request);
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(GROUP_ID, ExampleMatcher.GenericPropertyMatchers.exact())
                .withStringMatcher(StringMatcher.CONTAINING)
                .withIgnorePaths(REMOVE.getName())
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
            throw new ApiTestPlatformException(GET_USER_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = USER, template = "{{#userRequest.username}}")
    public Boolean add(UserRequest userRequest) {
        log.info("UserService-add()-params: [User]={}", userRequest.toString());
        try {
            isTrue(checkPassword(userRequest.getPassword()), "Passwords must contain lowercase, uppercase, number and "
                + "special characters.");
            isFalse(userRepository.existsByUsername(userRequest.getUsername()), "The username exists.");
            isFalse(userRepository.existsByEmail(userRequest.getEmail()), "The email exists.");
            UserEntity user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.insert(user);
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(ADD_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    private boolean checkPassword(String password) {
        Pattern pattern = Pattern
            .compile("^(?=.*\\d)(?=.*?[_\\-@&=!#$%^*<.>~`?'\"/;:'(){}\\[\\]])(?=.*[a-z])(?=.*[A-Z]).{8,40}$");
        return pattern.matcher(password).matches();
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = USER, template = "{{#userRequest.username}}")
    public Boolean edit(UserRequest userRequest) {
        log.info("UserService-edit()-params: [User]={}", userRequest.toString());
        try {
            UserEntity oldUser = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "User", userRequest.getId()));
            UserEntity user = userMapper.toEntity(userRequest);
            isFalse(!oldUser.getUsername().equals(userRequest.getUsername())
                && userRepository.existsByUsername(userRequest.getUsername()), "The username exists.");
            isFalse(!oldUser.getEmail().equals(userRequest.getEmail())
                && userRepository.existsByEmail(userRequest.getEmail()), "The email exists.");
            user.setPassword(oldUser.getPassword());
            user.setRemoved(oldUser.isRemoved());
            userRepository.save(user);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (DuplicateKeyException e) {
            log.error("The email:{} exist!", userRequest.getEmail());
            throw ExceptionUtils.mpe("The email:%s exist!", userRequest.getEmail());
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(EDIT_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = LOCK, operationModule = USER, template = "{{#result?.![#this.username]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean lock(List<String> ids) {
        try {
            return commonRepository.deleteByIds(ids, UserEntity.class);
        } catch (Exception e) {
            log.error("Failed to lock the User!", e);
            throw new ApiTestPlatformException(LOCK_USER_BY_ID_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = UNLOCK, operationModule = USER, template = "{{#result?.![#this.username]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean unlock(List<String> ids) {
        try {
            return commonRepository.recover(ids, UserEntity.class);
        } catch (Exception e) {
            log.error("Failed to unlock the User!", e);
            throw new ApiTestPlatformException(UNLOCK_USER_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean updatePassword(UserPasswordUpdateRequest request) {
        try {
            isTrue(StringUtils.equals(request.getNewPassword(), request.getConfirmPassword()),
                "The two passwords don't match.");
            isTrue(checkPassword(request.getNewPassword()), "Passwords must contain lowercase, uppercase, number and "
                + "special characters.");
            UserEntity userEntity = userRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.mpe("The user not exists."));
            isTrue(passwordEncoder.matches(request.getOldPassword(), userEntity.getPassword()),
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