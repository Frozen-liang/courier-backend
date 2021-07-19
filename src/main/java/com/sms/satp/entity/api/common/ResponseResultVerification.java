package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ResultVerificationType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ResponseResultVerification extends BaseVerification {

    private ResultVerificationType resultVerificationType;

    private ApiJsonType apiResponseJsonType;

    private List<MatchParamInfo> params;
}
