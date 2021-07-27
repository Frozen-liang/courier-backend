package com.sms.satp.repository;

import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedCaseTemplateRepository {

    Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);

    List<CaseTemplateEntity> getCaseTemplateIdsByGroupIds(List<String> ids);
}
