package com.sms.satp.repository;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.dto.response.ApiTestCaseResponse;
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
