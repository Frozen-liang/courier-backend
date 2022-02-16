package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WebhookResponse extends LookupUserResponse {

    private String url;

    private String description;

    @JsonProperty("isOnlyHandleError")
    @Field("isOnlyHandleError")
    private boolean onlyHandleError;

    private Integer webhookType;

    private String typeName;

    private String name;

    private Map<String, String> header;

    private String payload;
}
