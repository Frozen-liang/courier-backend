package com.sms.courier.entity.api.common;

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
public class ResponseHeadersVerification extends BaseVerification {

    private List<MatchParamInfo> params;
}
