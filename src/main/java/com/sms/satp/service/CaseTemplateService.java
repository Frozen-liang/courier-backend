package com.sms.satp.service;

import com.sms.satp.dto.AddCaseTemplateRequest;
import com.sms.satp.dto.CaseTemplateResponse;
import com.sms.satp.dto.CaseTemplateSearchDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.UpdateCaseTemplateRequest;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CaseTemplateService {

    Boolean add(AddCaseTemplateRequest addCaseTemplateRequest);

    Boolean deleteByIds(List<String> ids);

    Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest);

    Page<CaseTemplateResponse> page(PageDto pageDto, String projectId);

    Page<CaseTemplateResponse> search(CaseTemplateSearchDto searchDto, String projectId);
}
