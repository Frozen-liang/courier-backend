package com.sms.satp.repository;

import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedCaseTemplateRepository {

    Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId);
}
