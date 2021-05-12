package com.sms.satp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CaseTemplateConnDto {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer orderNumber;

    private Integer isExecute;

    private boolean remove;

    private Long createUserId;

    private LocalDateTime createDateTime;

    private Long modifyUserId;

    private LocalDateTime modifyDateTime;

    private List<CaseTemplateApiResponse> caseTemplateApiDtoList;
}
