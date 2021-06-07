package com.sms.satp.service;

import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import java.util.List;

public interface CaseTemplateConnService {

    Boolean deleteById(String id);

    Boolean deleteByIds(List<String> ids);

    List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId);

    List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId, boolean remove);

    List<CaseTemplateConn> listByCassTemplateId(String caseTemplateId);

    Boolean edit(CaseTemplateConn caseTemplateConn);

    Boolean editList(List<CaseTemplateConn> caseTemplateConn);

    Boolean add(AddCaseTemplateConnRequest addCaseTemplateConnRequest);

    CaseTemplateConn addByIds(String caseTemplateId, String sceneCaseId);
}
