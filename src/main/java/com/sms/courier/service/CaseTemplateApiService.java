package com.sms.courier.service;

import com.sms.courier.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.courier.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;

public interface CaseTemplateApiService {

    Boolean batchAdd(BatchAddCaseTemplateApiRequest addCaseTemplateApiRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateApiRequest updateCaseTemplateApiRequest);

    Boolean batchEdit(BatchUpdateCaseTemplateApiRequest updateCaseTemplateApiDto);

    List<CaseTemplateApiResponse> listResponseByCaseTemplateId(String caseTemplateId);

    List<CaseTemplateApiEntity> listByCaseTemplateId(String caseTemplateId, boolean remove);

    CaseTemplateApiResponse getCaseTemplateApiById(String id);

    void deleteAllByCaseTemplateIds(List<String> ids);
}
