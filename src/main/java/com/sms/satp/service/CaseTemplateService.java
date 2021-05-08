package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.entity.dto.CaseTemplateDto;
import com.sms.satp.entity.dto.CaseTemplateSearchDto;
import org.springframework.data.domain.Page;

public interface CaseTemplateService {

    void add(CaseTemplateDto sceneCaseTemplateDto);

    void deleteById(String id);

    void edit(CaseTemplateDto sceneCaseTemplateDto);

    Page<CaseTemplateDto> page(PageDto pageDto, String projectId);

    Page<CaseTemplateDto> search(CaseTemplateSearchDto searchDto, String projectId);
}
