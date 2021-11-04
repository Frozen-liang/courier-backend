package com.sms.courier.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiPageResponse extends LookupUserResponse {

    private String projectId;

    private String groupName;

    private String groupId;

    private List<String> tagName;

    private List<String> tagId;

    private String apiName;

    private String apiPath;

    private Integer apiProtocol;

    private Integer requestMethod;

    private Integer apiStatus;

    private String swaggerId;

    private String apiManagerId;

    private String apiManager;

    private Integer caseCount;

    private Integer sceneCaseCount;

    private Integer otherProjectSceneCaseCount;

    private String historyId;
}
