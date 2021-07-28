package com.sms.satp.entity.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private long delayTime;
    /**
     * 是否自动跟随请求重定向.
     */
    @Default
    @JsonProperty("isEnableRedirect")
    @Field(name = "isEnableRedirect")
    private boolean enableRedirect = true;

}
