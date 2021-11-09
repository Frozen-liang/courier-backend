package com.sms.courier.webhook.model;

import com.sms.courier.entity.BaseEntity;
import com.sms.courier.webhook.enums.WebhookType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "Webhook")
public class WebhookEntity extends BaseEntity {

    private String url;
    private List<WebhookType> webhookType;
}
