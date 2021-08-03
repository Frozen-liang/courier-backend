package com.sms.courier.entity.group;

import com.sms.courier.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @EqualsAndHashCode.Include
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    @EqualsAndHashCode.Include
    private String name;
    @Indexed(unique = true)
    @EqualsAndHashCode.Exclude
    private Long realGroupId;
    @Default
    @EqualsAndHashCode.Exclude
    private List<Long> path = new ArrayList<>();
    @Field(targetType = FieldType.OBJECT_ID)
    private String parentId;
    @Default
    private Integer depth = 1;
}
