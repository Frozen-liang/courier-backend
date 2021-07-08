package com.sms.satp.dto.response;

import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CaseTemplateConnResponse extends BaseResponse {

    private String sceneCaseId;

    private String caseTemplateId;

    private List<CaseTemplateApiConn> caseTemplateApiConnList;

    private List<CaseTemplateApiResponse> caseTemplateApiDtoList;
}
