package com.sms.courier.dto.request;

import com.sms.courier.common.enums.ApiJsonType;
import com.sms.courier.common.enums.ResultVerificationType;
import com.sms.courier.common.enums.VerificationElementType;
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
public class ResponseResultVerificationRequest extends BaseVerification {

    private ResultVerificationType resultVerificationType;

    private ApiJsonType apiResponseJsonType;

    private VerificationElementType verificationElementType;

    private List<MatchParamInfoRequest> params;
}
