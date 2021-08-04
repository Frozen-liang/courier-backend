package com.sms.courier.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CaseRecordRequest {

    private String destination;

    private Integer caseCount;

    private Integer sceneCaseCount;
}
