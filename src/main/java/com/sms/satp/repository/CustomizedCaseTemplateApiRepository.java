package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;

public interface CustomizedCaseTemplateApiRepository {

    List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecuteAndIsRemove(String caseTemplateId, boolean isExecute,
        boolean isRemove);

    List<CaseTemplateApiEntity> findCaseTemplateApiIdsByCaseTemplateIds(List<String> ids);

    Boolean deleteByIds(List<String> caseTemplateApiIds);

    Boolean recover(List<String> caseTemplateApiIds);

}
