package com.sms.satp.service;

import com.sms.satp.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {

    List<RoleResponse> findRolesByGroupId(String groupId);
}
