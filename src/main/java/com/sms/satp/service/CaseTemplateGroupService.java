package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateGroupRequest;
import com.sms.satp.dto.request.SearchCaseTemplateGroupRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import java.util.List;

public interface CaseTemplateGroupService {

    Boolean add(AddCaseTemplateGroupRequest request);

    Boolean edit(UpdateCaseTemplateGroupRequest request);

    Boolean deleteById(String id);

    List<CaseTemplateGroupResponse> getList(SearchCaseTemplateGroupRequest request);
}
