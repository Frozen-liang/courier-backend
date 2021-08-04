package com.sms.courier.service.impl;

import com.sms.courier.common.enums.RoleType;
import com.sms.courier.dto.response.RoleResponse;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.mapper.RoleMapper;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.service.RoleService;
import com.sms.courier.service.UserGroupService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
        List<RoleResponse> roleResponses = roleMapper
            .toDtoList(roleRepository.findAllByRoleType(RoleType.USER).collect(Collectors.toList()));
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
