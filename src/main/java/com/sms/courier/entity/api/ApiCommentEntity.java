package com.sms.courier.entity.api;

import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ApiComment")
public class ApiCommentEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String apiId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String parentId;

    private String comment;

}
