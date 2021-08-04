package com.sms.courier.dto.response;

import com.sms.courier.entity.api.common.BaseVerification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeadersVerificationResponse extends BaseVerification {

    private List<MatchParamInfoResponse> params;
}
