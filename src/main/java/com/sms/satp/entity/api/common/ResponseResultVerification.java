package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ApiJsonType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultVerification {

    private Boolean checkStatus;

    private ApiJsonType apiResponseJsonType;

    private List<MatchParamInfo> params;
}
