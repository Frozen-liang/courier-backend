package com.sms.satp.service;

import com.sms.satp.dto.response.ApiGroupResponse;
import java.util.List;

public interface ApiGroupService {

    List<ApiGroupResponse> list(String projectId);
}
