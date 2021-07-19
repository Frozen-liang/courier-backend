package com.sms.satp.entity.api.common;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ResponseTimeVerification extends BaseVerification {

    @Field("isCheckStatus")
    private boolean checkStatus;

    private Integer timeoutLimit;
}
