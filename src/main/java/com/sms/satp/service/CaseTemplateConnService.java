package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import java.util.List;

public interface CaseTemplateConnService {

    Boolean deleteById(String id);

    List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId);

    List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId, boolean remove);

    Boolean edit(CaseTemplateConn caseTemplateConn);

    Boolean editList(List<CaseTemplateConn> caseTemplateConn);

    Boolean add(AddCaseTemplateConnRequest addCaseTemplateConnRequest);

}
