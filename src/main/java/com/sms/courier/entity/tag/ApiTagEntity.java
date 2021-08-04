package com.sms.courier.entity.tag;

import com.sms.courier.common.enums.ApiTagType;
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

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiTag")
public class ApiTagEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;
    private ApiTagType tagType;
    private String tagName;
}
