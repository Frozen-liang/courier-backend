package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ResponseParamsExtractionType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.entity.api.common.AdvancedSetting;
import com.sms.satp.entity.api.common.HttpStatusVerification;
import com.sms.satp.entity.api.common.ResponseHeadersVerification;
import com.sms.satp.entity.api.common.ResponseResultVerification;
import com.sms.satp.entity.api.common.ResponseTimeVerification;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTestCaseRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id can not be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The caseName can not be empty.")
    private String caseName;
    private String dataCollId;
    private List<String> tagId;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId can not be empty.")
    private String projectId;
    private ApiBindingStatus status;
    private ResponseParamsExtractionType responseParamsExtractionType;
    private HttpStatusVerification httpStatusVerification;
    private ResponseHeadersVerification responseHeadersVerification;
    private ResponseResultVerification responseResultVerification;
    private ResponseTimeVerification responseTimeVerification;
    @Default
    @JsonProperty("isExecute")
    private boolean execute = true;
    private AdvancedSetting advancedSetting;
    private ApiRequest apiEntity;
}
