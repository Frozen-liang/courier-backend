package com.sms.courier.entity.job;

import com.sms.courier.common.enums.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDatabase {

    @Field(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private String name;

    private DatabaseType databaseType;

    private String url;

    private String port;

    private String username;

    private String password;

    private String dataBaseName;
}
