package com.sms.satp.repository;

import com.sms.satp.entity.dto.CaseTemplateSearchDto;
import com.sms.satp.entity.scenetest.CaseTemplate;
import org.springframework.data.domain.Page;

public interface CustomizedCaseTemplateRepository {

    Page<CaseTemplate> search(CaseTemplateSearchDto searchDto, String projectId);
}
