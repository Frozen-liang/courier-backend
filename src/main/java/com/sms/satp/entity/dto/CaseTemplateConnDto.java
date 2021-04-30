package com.sms.satp.entity.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

    @CreatedBy
    private Long createUserId;

    @CreatedDate
    private LocalDateTime createDateTime;

    @LastModifiedBy
    private Long modifyUserId;

    @LastModifiedDate
    private LocalDateTime modifyDateTime;

    private List<CaseTemplateApiDto> caseTemplateApiDtoList;
}
