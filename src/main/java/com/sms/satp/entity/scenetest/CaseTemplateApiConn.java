package com.sms.satp.entity.scenetest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseTemplateApiConn {

    private String caseTemplateApiId;

    private Integer order;

    private Boolean isExecute;
}
