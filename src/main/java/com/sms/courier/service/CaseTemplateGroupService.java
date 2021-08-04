package com.sms.courier.service;

import com.sms.courier.dto.request.CaseTemplateGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import java.util.List;

public interface CaseTemplateGroupService {

    Boolean add(CaseTemplateGroupRequest request);

    Boolean edit(CaseTemplateGroupRequest request);

    Boolean deleteById(String id);

    List<TreeResponse> list(String projectId);
}
