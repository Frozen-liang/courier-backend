package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateDetailResponse;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.dto.response.IdResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CaseTemplateService {

    Boolean add(AddCaseTemplateRequest addCaseTemplateRequest);

    IdResponse add(String sceneCaseId);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    Boolean batchEdit(List<CaseTemplate> caseTemplateList);

    Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId);

    List<CaseTemplate> get(String groupId, String projectId);

    CaseTemplateDetailResponse getApiList(String caseTemplateId, boolean removed);

}
