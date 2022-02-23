package com.sms.courier.service;

import com.sms.courier.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.request.ConvertCaseTemplateRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateDetailResponse;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.response.IdResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CaseTemplateService {

    Boolean add(AddCaseTemplateRequest addCaseTemplateRequest);

    IdResponse add(ConvertCaseTemplateRequest convertCaseTemplateRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId);

    CaseTemplateDetailResponse getApiList(String caseTemplateId);

    Boolean addApi(AddCaseTemplateApiByIdsRequest request);

    Boolean delete(List<String> ids);

    Boolean recover(List<String> ids);
}
