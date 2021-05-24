package com.sms.satp.dto.response;

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

    private Boolean checkStatus;

    private List<MatchParamInfoResponse> params;
}
