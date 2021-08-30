package com.sms.courier.repository;

import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedApiTestCaseRepository {

    void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);

    List<ApiTestCaseResponse> listByJoin(ObjectId apiId, ObjectId projectId, boolean removed);
}