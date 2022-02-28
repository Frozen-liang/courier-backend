package com.sms.courier.entity.file;

import com.sms.courier.entity.BaseEntity;
import com.sms.courier.storagestrategy.StorageType;
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
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "FileInfo")
public class FileInfoEntity extends BaseEntity {
    private String filename;
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private Long length;
    private String sourceId;
    private StorageType type;
}
