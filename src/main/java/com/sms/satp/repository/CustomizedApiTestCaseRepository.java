package com.sms.satp.repository;

import com.sms.satp.common.enums.ApiBindingStatus;
import java.util.List;

public interface CustomizedApiTestCaseRepository {

    void updateApiTestCaseStatusByApiId(List<String> apiIds, ApiBindingStatus status);

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);
}
