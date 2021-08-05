package com.sms.courier.service;

import com.sms.courier.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {

    List<RoleResponse> findRolesByGroupId(String groupId);
}
