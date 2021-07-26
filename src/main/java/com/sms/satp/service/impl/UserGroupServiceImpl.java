package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.USER_GROUP;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_USER_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_USER_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_USER_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_GROUP_LIST_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.UserGroupRequest;
import com.sms.satp.dto.response.UserGroupResponse;
import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.mapper.UserGroupMapper;
import com.sms.satp.repository.CommonRepository;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.repository.UserGroupRepository;
import com.sms.satp.service.UserGroupService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.SecurityUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        CommonRepository commonRepository, userGroupMapper userGroupMapper, SystemRoleRepository systemRoleRepository) {
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
            return userGroupMapper.toDtoList(userGroupRepository.findAllByRemovedIsFalseOrderByCreateDateTimeDesc());
        } catch (Exception e) {
            log.error("Failed to get the UserGroup list!", e);
            throw new ApiTestPlatformException(GET_USER_GROUP_LIST_ERROR);
        }
    }


    @Override
    @LogRecord(operationType = ADD, operationModule = USER_GROUP, template = "{{#userGroupRequest.name}}")
    public Boolean add(UserGroupRequest userGroupRequest) {
        log.info("UserGroupService-add()-params: [UserGroup]={}", userGroupRequest.toString());
        try {
            UserGroupEntity userGroup = userGroupMapper.toEntity(userGroupRequest);
            userGroup.setCreateUsername(SecurityUtil.getCurrentUser().getUsername());
            userGroupRepository.insert(userGroup);
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
        } catch (ApiTestPlatformException apiTestPlatEx) {
            log.error(apiTestPlatEx.getMessage());
            throw apiTestPlatEx;
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
            return commonRepository.deleteByIds(ids, UserGroupEntity.class);
        } catch (Exception e) {
            log.error("Failed to delete the UserGroup!", e);
            throw new ApiTestPlatformException(DELETE_USER_GROUP_BY_ID_ERROR);
        }
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthoritiesByUserGroup(String groupId) {
        try {
            UserGroupEntity userGroupEntity = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException(
                    String.format("The user group - %s was not found.", groupId)));
            // Query permissions by group id
            Iterable<SystemRoleEntity> roles = systemRoleRepository.findAllById(userGroupEntity.getRoleIds());
            Collection<String> roleNames = CollectionUtils.collect(roles, SystemRoleEntity::getName);
            return CollectionUtils.isNotEmpty(roleNames) ? roleNames.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()) : Collections.emptyList();
        } catch (Exception exception) {
            return Collections.emptyList();
        }
    }

}