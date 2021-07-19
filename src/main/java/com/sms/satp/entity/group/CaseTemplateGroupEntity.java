package com.sms.satp.entity.group;

import com.sms.satp.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document(collection = "CaseTemplateGroup")
public class CaseTemplateGroupEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private String name;

    @Field(targetType = FieldType.OBJECT_ID)
    private String parentId;
}
