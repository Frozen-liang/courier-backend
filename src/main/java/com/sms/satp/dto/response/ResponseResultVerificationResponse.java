package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultVerificationResponse {

    @JsonProperty("isCheckStatus")
    private boolean checkStatus;

    private Integer resultVerificationType;

    private Integer apiResponseJsonType;

    private List<MatchParamInfoResponse> params;
}
