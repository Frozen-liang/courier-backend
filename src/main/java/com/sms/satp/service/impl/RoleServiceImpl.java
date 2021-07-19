package com.sms.satp.service.impl;

import com.sms.satp.dto.response.RoleResponse;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.mapper.RoleMapper;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.service.RoleService;
import com.sms.satp.service.UserGroupService;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final SystemRoleRepository roleRepository;
    private final UserGroupService userGroupService;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(SystemRoleRepository roleRepository, UserGroupService userGroupService,
        RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.userGroupService = userGroupService;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleResponse> findRolesByGroupId(String groupId) {
        UserGroupEntity userGroup = userGroupService.findById(groupId);
        List<RoleResponse> roleResponses = roleMapper.toDtoList(roleRepository.findAll());
        if (Objects.nonNull(userGroup) && CollectionUtils.isNotEmpty(userGroup.getRoleIds())) {
            roleResponses.forEach(role -> {
                if (userGroup.getRoleIds().contains(role.getId())) {
                    role.setExist(true);
                }
            });
        }
        return roleResponses;
    }
}
