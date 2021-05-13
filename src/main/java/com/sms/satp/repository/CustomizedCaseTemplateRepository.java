package com.sms.satp.repository;

import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.entity.scenetest.CaseTemplate;
import org.springframework.data.domain.Page;

public interface CustomizedCaseTemplateRepository {

    Page<CaseTemplate> search(CaseTemplateSearchRequest searchDto, String projectId);
}
