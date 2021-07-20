package com.sms.satp.entity.system;

import com.sms.satp.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "User")
public class UserEntity extends BaseEntity {

    private String username;
    private String nickname;
    @HashIndexed
    @Indexed(unique = true)
    private String email;
    private String password;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;
}
