package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ApiType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseApiRequest {

    @NotNull(message = "The id can not be empty")
    private String id;

    private String sceneCaseId;

    private String projectId;

    private ApiType apiType;

    private String shell;

    private String databaseId;

    private String sql;

    @JsonProperty("isSqlResult")
    private boolean sqlResult;

    private Integer order;

    /**
     * 是否强制执行该步骤，即使其他步骤出错时.
     */
    @JsonProperty("isLock")
    private boolean lock;

    private String aliasName;
    /**
     * API绑定状态.
     */
    private ApiBindingStatus apiBindingStatus;

    private ApiTestCaseRequest apiTestCase;

}
