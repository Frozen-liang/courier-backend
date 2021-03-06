package com.sms.courier.entity.system;

import com.sms.courier.common.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
    private RoleType roleType;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDataTime;
}
