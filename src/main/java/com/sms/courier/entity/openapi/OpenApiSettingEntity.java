package com.sms.courier.entity.openapi;

import com.sms.courier.entity.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "OpenApiSetting")
public class OpenApiSettingEntity extends BaseEntity {

    @Indexed(unique = true)
    private String name;
    private String description;
    private LocalDateTime expireTime;
    @Persistent
    private String token;
}
