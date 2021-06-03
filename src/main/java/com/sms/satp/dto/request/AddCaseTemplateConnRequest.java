package com.sms.satp.dto.request;

import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCaseTemplateConnRequest {

    private String sceneCaseId;

    private String caseTemplateId;

    private List<CaseTemplateApiConn> caseTemplateApiConnList;

    private Boolean removed;
}
