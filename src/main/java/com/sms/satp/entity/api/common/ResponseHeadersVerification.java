package com.sms.satp.entity.api.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeadersVerification {

    private Boolean checkStatus;

    private List<MatchParamInfo> params;
}
