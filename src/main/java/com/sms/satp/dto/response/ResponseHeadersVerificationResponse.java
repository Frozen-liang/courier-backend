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
public class ResponseHeadersVerificationResponse {

    @JsonProperty("isCheckStatus")
    private boolean checkStatus;

    private List<MatchParamInfoResponse> params;
}
