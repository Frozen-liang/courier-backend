package com.sms.satp.service;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import java.util.List;

public interface ApiTestCaseService {

    ApiTestCaseResponse findById(String id);

    List<ApiTestCaseResponse> list(String apiId, String projectId);

    Boolean add(ApiTestCaseRequest apiTestCaseRequest);

    Boolean edit(ApiTestCaseRequest apiTestCaseRequest);

    Boolean delete(List<String> ids);

    void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status);
}