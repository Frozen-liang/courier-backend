package com.sms.satp.entity.tag;

import com.sms.satp.common.enums.ApiTagType;
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

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiTag")
public class ApiTag extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;
    private String tagName;
    private ApiTagType tagType;
}
