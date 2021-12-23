package com.sms.courier.entity.database;

import com.sms.courier.common.enums.DatabaseType;
import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Document(collection = "DataBase")
public class DatabaseEntity extends BaseEntity {

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
