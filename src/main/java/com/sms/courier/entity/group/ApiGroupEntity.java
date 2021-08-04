package com.sms.courier.entity.group;

import com.sms.courier.entity.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
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

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Document("ApiGroup")
public class ApiGroupEntity extends BaseEntity {


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
