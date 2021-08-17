package com.sms.courier.entity.api.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sms.courier.utils.DurationToStringSerializer;
import com.sms.courier.utils.StringToDurationSerializer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Duration;
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
public class ResponseTimeVerification extends BaseVerification {


    @JsonSerialize(using = DurationToStringSerializer.class)
    @JsonDeserialize(using = StringToDurationSerializer.class)
    private Duration timeoutLimit;
}
