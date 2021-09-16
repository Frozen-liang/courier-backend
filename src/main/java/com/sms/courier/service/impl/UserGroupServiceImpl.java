package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.USER_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_USER_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_USER_GROUP_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_USER_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_USER_GROUP_LIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_NAME_EXISTS_ERROR;
import static com.sms.courier.common.field.CommonField.GROUP_ID;
import static com.sms.courier.utils.Assert.isFalse;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.CollectionName;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.UserGroupRequest;
import com.sms.courier.dto.response.UserGroupResponse;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.mapper.UserGroupMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.repository.UserGroupRepository;
import com.sms.courier.service.UserGroupService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final CommonRepository commonRepository;
    private final UserGroupMapper userGroupMapper;
    private final SystemRoleRepository systemRoleRepository;

    public UserGroupServiceImpl(UserGroupRepository userGroupRepository,
        CommonRepository commonRepository, UserGroupMapper userGroupMapper, SystemRoleRepository systemRoleRepository) {
        this.userGroupRepository = userGroupRepository;
        this.commonRepository = commonRepository;
        this.userGroupMapper = userGroupMapper;
        this.systemRoleRepository = systemRoleRepository;
    }

    @Override
    public UserGroupEntity findById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return userGroupRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserGroupResponse> list() {
        try {
            return commonRepository.listLookupUser(CollectionName.USER_GROUP.getName(),
                Collections.emptyList(),
                UserGroupResponse.class);
        } catch (Exception e) {
            log.error("Failed to get the UserGroup list!", e);
            throw new ApiTestPlatformException(GET_USER_GROUP_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = USER_GROUP, template = "{{#userGroupRequest.name}}")
    public Boolean add(UserGroupRequest userGroupRequest) {
        log.info("UserGroupService-add()-params: [UserGroup]={}", userGroupRequest.toString());
        String name = userGroupRequest.getName();
        try {
            UserGroupEntity userGroup = userGroupMapper.toEntity(userGroupRequest);
            isFalse(userGroupRepository.existsByName(name), THE_NAME_EXISTS_ERROR, name);
            userGroupRepository.insert(userGroup);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (Exception e) {
            log.error("Failed to add the UserGroup!", e);
            throw new ApiTestPlatformException(ADD_USER_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = USER_GROUP, template = "{{#userGroupRequest.name}}")
    public Boolean edit(UserGroupRequest userGroupRequest) {
        log.info("UserGroupService-edit()-params: [UserGroup]={}", userGroupRequest.toString());
        try {
            UserGroupEntity oldUserGroup = userGroupRepository.findById(userGroupRequest.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "UserGroup", userGroupRequest.getId()));
            UserGroupEntity userGroup = userGroupMapper.toEntity(userGroupRequest);
            userGroup.setDefaultGroup(oldUserGroup.isDefaultGroup());
            userGroupRepository.save(userGroup);
        } catch (ApiTestPlatformException courierException) {
            log.error(courierException.getMessage());
            throw courierException;
        } catch (DuplicateKeyException e) {
            log.error("The name {} exists.", userGroupRequest.getName());
            throw ExceptionUtils.mpe(THE_NAME_EXISTS_ERROR, userGroupRequest.getName());
        } catch (Exception e) {
            log.error("Failed to add the UserGroup!", e);
            throw new ApiTestPlatformException(EDIT_USER_GROUP_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = USER_GROUP, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean delete(List<String> ids) {
        try {
            Update update = new Update();
            update.unset(GROUP_ID.getName());
            Query query = new Query();
            GROUP_ID.in(ids).ifPresent(query::addCriteria);
            commonRepository.updateField(query, update, UserEntity.class);
            return userGroupRepository.deleteByIdIn(ids) > 0;
        } catch (Exception e) {
            log.error("Failed to delete the UserGroup!", e);
            throw new ApiTestPlatformException(DELETE_USER_GROUP_BY_ID_ERROR);
        }
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthoritiesByUserGroup(String groupId) {
        try {
            Objects.requireNonNull(groupId, "The groupId must not be null.");
            UserGroupEntity userGroupEntity = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException(
                    String.format("The user group - %s was not found.", groupId)));
            // Query permissions by group id
            Iterable<SystemRoleEntity> roles = systemRoleRepository.findAllById(userGroupEntity.getRoleIds());
            Collection<String> roleNames = CollectionUtils.collect(roles, SystemRoleEntity::getName);
            return CollectionUtils.isNotEmpty(roleNames) ? roleNames.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()) : Collections.emptyList();
        } catch (Exception exception) {
            log.error("Query authorities by id error.", exception);
            return Collections.emptyList();
        }
    }

}