package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import java.util.List;

public interface CustomizedCaseTemplateApiRepository {

    List<CaseTemplateApiEntity> findByCaseTemplateIdAndIsExecuteAndIsRemove(String caseTemplateId, boolean isExecute,
        boolean isRemove);

    List<CaseTemplateApiEntity> findCaseTemplateApiIdsByCaseTemplateIds(List<String> ids);

    Boolean deleteByIds(List<String> caseTemplateApiIds);

    Boolean recover(List<String> caseTemplateApiIds);

    List<String> findCaseTemplateIdByApiIds(List<String> apiIds);
}
