package com.sms.courier.entity.system;

import com.sms.courier.entity.BaseEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "UserGroup")
@AllArgsConstructor
public class UserGroupEntity extends BaseEntity {

    @Indexed(unique = true, background = true)
    private String name;

    private List<String> roleIds;

    @Field(name = "isDefaultGroup")
    private boolean defaultGroup;
}
