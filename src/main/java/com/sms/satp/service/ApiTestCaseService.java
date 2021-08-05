package com.sms.satp.service;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiTestCaseService {

    ApiTestCaseResponse findById(String id);

    List<ApiTestCaseResponse> list(ObjectId apiId, ObjectId projectId, boolean removed);

    Boolean add(ApiTestCaseRequest apiTestCaseRequest);

    Boolean edit(ApiTestCaseRequest apiTestCaseRequest);

    Boolean delete(List<String> ids);

    void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status);

    Boolean deleteByIds(List<String> ids);

    Boolean deleteAll();

    Boolean recover(List<String> ids);
}