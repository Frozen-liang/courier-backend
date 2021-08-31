package com.sms.courier.repository;

import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface CustomizedCaseTemplateRepository {

    Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId);

    Boolean deleteByIds(List<String> ids);

    Boolean recover(List<String> ids);

    List<CaseTemplateEntity> getCaseTemplateIdsByGroupIds(List<String> ids);

    Boolean deleteGroupIdByIds(List<String> ids);

    Optional<CaseTemplateResponse> findById(String caseTemplateId);
}
