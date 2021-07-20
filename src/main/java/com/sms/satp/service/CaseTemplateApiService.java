package com.sms.satp.service;

import com.sms.satp.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;

public interface CaseTemplateApiService {

    Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto);

    List<CaseTemplateApiResponse> listResponseByCaseTemplateId(String caseTemplateId);

    List<CaseTemplateApiEntity> listByCaseTemplateId(String caseTemplateId);

    List<CaseTemplateApiEntity> getApiByCaseTemplateId(String caseTemplateId, boolean remove);

    CaseTemplateApiResponse getCaseTemplateApiById(String id);

}
