package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.USER;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_LIST_ERROR;
import static com.sms.satp.common.field.CommonFiled.CREATE_DATE_TIME;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.mapper.UserMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.UserService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.SecurityUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CommonDeleteRepository commonDeleteRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
        CommonDeleteRepository commonDeleteRepository,
        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.commonDeleteRepository = commonDeleteRepository;
        this.userMapper = userMapper;
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
    public List<UserResponse> list() {
        try {
            Sort sort = Sort.by(Direction.DESC, CREATE_DATE_TIME.getFiled());
            return userMapper.toDtoList(userRepository.findAll(sort));
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
            UserEntity user = userMapper.toEntity(userRequest);
            userRepository.insert(user);
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(ADD_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = USER, template = "{{#userRequest.username}}")
    public Boolean edit(UserRequest userRequest) {
        log.info("UserService-edit()-params: [User]={}", userRequest.toString());
        try {
            boolean exists = userRepository.existsById(userRequest.getId());
            if (!exists) {
                throw ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "User", userRequest.getId());
            }
            UserEntity user = userMapper.toEntity(userRequest);
            userRepository.save(user);
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
        } catch (Exception e) {
            log.error("Failed to add the User!", e);
            throw new ApiTestPlatformException(EDIT_USER_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = USER, template = "{{#result?.![#this.username]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            return commonDeleteRepository.deleteByIds(ids, UserEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the User!", e);
            throw new ApiTestPlatformException(DELETE_USER_BY_ID_ERROR);
        }
    }

}