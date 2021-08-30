package com.sms.courier.service;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.apitestcase.TestResult;
import java.util.List;
import org.bson.types.ObjectId;

public interface ApiTestCaseService {

    ApiTestCaseResponse findById(String id);

    ApiTestCaseEntity findOne(String id);

    List<ApiTestCaseResponse> list(ObjectId apiId, ObjectId projectId, boolean removed);

    Boolean add(ApiTestCaseRequest apiTestCaseRequest);

    Boolean edit(ApiTestCaseRequest apiTestCaseRequest);

    Boolean delete(List<String> ids);

    void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status);

    Boolean deleteByIds(List<String> ids);

    Boolean deleteAll();

    Boolean recover(List<String> ids);

    Long count(String projectId);

    void insertTestResult(String id, TestResult testResult);
}