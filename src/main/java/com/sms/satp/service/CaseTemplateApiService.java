package com.sms.satp.service;

import com.sms.satp.entity.dto.AddCaseTemplateApiDto;
import com.sms.satp.entity.dto.CaseTemplateApiDto;
import com.sms.satp.entity.dto.UpdateCaseTemplateApiDto;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import java.util.List;

public interface CaseTemplateApiService {

    void batch(AddCaseTemplateApiDto addSceneCaseApiDto);

    void deleteById(String id);

    void edit(CaseTemplateApiDto caseTemplateApiDto);

    void batchEdit(UpdateCaseTemplateApiDto updateCaseTemplateApiDto);

    List<CaseTemplateApiDto> listByCaseTemplateId(String caseTemplateId, boolean remove);

    List<CaseTemplateApi> listByCaseTemplateId(String caseTemplateId);

    CaseTemplateApiDto getSceneCaseApiById(String id);


}
