package com.sms.courier.repository;

import com.sms.courier.dto.response.LoadFunctionResponse;
import java.util.List;

public interface CustomizedFunctionRepository {

    List<LoadFunctionResponse> loadFunction(String projectId, String workspaceId, Class<?> entityClass);
}
