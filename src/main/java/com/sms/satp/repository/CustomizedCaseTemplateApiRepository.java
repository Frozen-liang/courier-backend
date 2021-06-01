package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;

public interface CustomizedCaseTemplateApiRepository {

    List<CaseTemplateApi> findByCaseTemplateIds(List<String> caseTemplateIds);
}
