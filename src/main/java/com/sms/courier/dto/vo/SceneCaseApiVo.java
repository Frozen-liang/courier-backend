package com.sms.courier.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.dto.response.ApiTestCaseResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneCaseApiVo {

    private String id;

    private String sceneCaseId;

    private String caseTemplateId;

    private String projectId;

    private Integer apiType;

    private String shell;

    private String databaseId;

    private String sql;

    @JsonProperty("isSqlResult")
    private boolean sqlResult;

    private Integer order;

    @JsonProperty("isLock")
    private boolean lock;

    private String aliasName;

    private ApiTestCaseResponse apiTestCase;

    private List<CaseTemplateApiConnVo> caseTemplateApiConnList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CaseTemplateApiConnVo {

        private String caseTemplateApiId;

        @JsonProperty("isExecute")
        private boolean execute;

        @JsonProperty("isLock")
        private boolean lock;
    }
}
