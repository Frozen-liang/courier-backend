package com.sms.courier.webhook.model;

import com.sms.courier.webhook.enums.WebhookType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "WebhookType")
public class WebhookTypeEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Indexed(unique = true)
    private WebhookType type;
    private String name;
    private String defaultPayload;
    private List<Map<String, Object>> fieldDesc;
}
