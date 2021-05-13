package com.sms.satp.dto.response;

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
public class CaseTemplateConnResponse {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private Integer orderNumber;

    private Boolean isExecute;

    private Boolean removed;

    private Long createUserId;

    private LocalDateTime createDateTime;

    private Long modifyUserId;

    private LocalDateTime modifyDateTime;

    private List<CaseTemplateApiResponse> caseTemplateApiDtoList;
}
