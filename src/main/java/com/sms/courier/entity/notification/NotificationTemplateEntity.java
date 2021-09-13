package com.sms.courier.entity.notification;

import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "NotificationTemplate")
public class NotificationTemplateEntity extends BaseEntity {

    private Integer type;
    private String title;
    private String content;
    private String titleVariableKey;
    private String contentVariableKey;

}