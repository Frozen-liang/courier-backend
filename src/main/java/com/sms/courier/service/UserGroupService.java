package com.sms.courier.service;

import com.sms.courier.dto.request.UserGroupRequest;
import com.sms.courier.dto.response.UserGroupResponse;
import com.sms.courier.entity.system.UserGroupEntity;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface UserGroupService {

    UserGroupEntity findById(String id);

    List<UserGroupResponse> list();

    Boolean add(UserGroupRequest userGroupRequest);

    Boolean edit(UserGroupRequest userGroupRequest);

    Boolean delete(List<String> ids);

    List<SimpleGrantedAuthority> getAuthoritiesByUserGroup(String groupId);
}