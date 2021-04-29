package com.sms.satp.service;

import com.sms.satp.entity.scenetest.CaseTemplateConn;
import java.util.List;

public interface CaseTemplateConnService {

    void deleteById(String id);

    List<CaseTemplateConn> listBySceneCaseId(String sceneCaseId);
}
