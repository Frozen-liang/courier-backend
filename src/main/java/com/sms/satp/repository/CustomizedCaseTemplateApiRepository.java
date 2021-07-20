package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;

public interface CustomizedCaseTemplateApiRepository {

    List<CaseTemplateApiEntity> findByCaseTemplateIds(List<String> caseTemplateIds);

    List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecute(String caseTemplateId, Boolean isExecute);

    int findCurrentOrderByCaseTemplateId(String caseTemplateId);
}
