package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceProjectCaseStatisticsResponse {

    private String projectId;

    private String projectName;

    private int apiCount;

    private int caseCount;

    private double percentage;
}
