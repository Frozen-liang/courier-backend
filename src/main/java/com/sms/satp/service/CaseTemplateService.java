package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CaseTemplateService {

    Boolean add(AddCaseTemplateRequest addCaseTemplateRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    Page<CaseTemplateResponse> page(PageDto pageDto, String projectId);

    Page<CaseTemplateResponse> search(CaseTemplateSearchRequest searchDto, String projectId);
}
