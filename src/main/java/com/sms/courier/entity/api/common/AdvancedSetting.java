package com.sms.courier.entity.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sms.courier.utils.DurationToStringSerializer;
import com.sms.courier.utils.StringToDurationSerializer;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSetting {

    /**
     * 延期时间，单位：毫秒.
     */
    @JsonSerialize(using = DurationToStringSerializer.class)
    @JsonDeserialize(using = StringToDurationSerializer.class)
    private Duration delayTime;
    /**
     * 是否自动跟随请求重定向.
     */
    @Default
    @JsonProperty("isEnableRedirect")
    @Field(name = "isEnableRedirect")
    private boolean enableRedirect = true;

}
