package com.sms.satp.dto.response;

import com.sms.satp.entity.api.common.BaseVerification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultVerificationResponse extends BaseVerification {

    private Integer resultVerificationType;

    private Integer apiResponseJsonType;

    private List<MatchParamInfoResponse> params;
}
