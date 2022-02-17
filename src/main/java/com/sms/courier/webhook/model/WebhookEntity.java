package com.sms.courier.webhook.model;

import com.sms.courier.entity.BaseEntity;
import com.sms.courier.webhook.enums.WebhookType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "Webhook")
public class WebhookEntity extends BaseEntity {

    private String name;
    private String url;
    private String description;
    @Field("isOnlyHandleError")
    private boolean onlyHandleError;
    private Map<String, String> header;
    private WebhookType webhookType;
    private String payload;
}
