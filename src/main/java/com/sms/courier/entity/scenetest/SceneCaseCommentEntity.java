package com.sms.courier.entity.scenetest;

import com.sms.courier.common.enums.ReviewStatus;
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
@Document(collection = "SceneCaseComment")
public class SceneCaseCommentEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String sceneCaseId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String parentId;

    private String comment;

    private ReviewStatus reviewStatus;

}
