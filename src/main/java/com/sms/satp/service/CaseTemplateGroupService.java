package com.sms.satp.service;

import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.dto.response.TreeResponse;
import java.util.List;

public interface CaseTemplateGroupService {

    Boolean add(CaseTemplateGroupRequest request);

    Boolean edit(CaseTemplateGroupRequest request);

    Boolean deleteById(String id);

    List<TreeResponse> list(String projectId);
}
