package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.satp.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;

public interface CaseTemplateApiService {

    Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    Boolean editAll(List<CaseTemplateApi> caseTemplateApiList);

    Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto);

    List<CaseTemplateApiResponse> listByCaseTemplateId(String caseTemplateId, boolean removed);

    List<CaseTemplateApi> listByCaseTemplateId(String caseTemplateId);

    List<CaseTemplateApi> getApiByCaseTemplateId(String caseTemplateId, boolean remove);

    CaseTemplateApiResponse getCaseTemplateApiById(String id);

    Boolean add(AddCaseTemplateApiByIdsRequest request);
}
