package com.sms.satp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCaseTemplateConnRequest {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer orderNumber;

    private Boolean isExecute;

    private Boolean removed;
}
