package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LogResponse {

    private String id;

    private String projectId;

    private Integer operationType;

    private Integer operationModule;

    private String operationDesc;

    private String operator;

    private String operatorId;

    private String operationDateTime;
}
