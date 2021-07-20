package com.sms.satp.entity.system;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Document(collection = "SystemRole")
public class SystemRoleEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String description;
    @Default
    @Field("isEnable")
    private boolean enable = true;
    @Field("isDefaultRole")
    private boolean defaultRole;
    @CreatedDate
    private LocalDateTime createDateTime;
}
