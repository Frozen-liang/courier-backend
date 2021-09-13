package com.sms.courier.entity.notification;

import com.sms.courier.config.EmailProperties;
import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "EmailService")
public class EmailServiceEntity extends BaseEntity {

    private EmailProperties properties;
    @Builder.Default
    @Field(name = "isEnabled")
    private Boolean enabled = Boolean.TRUE;

}