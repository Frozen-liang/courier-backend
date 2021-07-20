package com.sms.satp.entity.group;

import com.sms.satp.entity.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@CompoundIndex(def = "{'projectId': 1, 'name': 1}", unique = true)
@Document("ApiGroup")
public class ApiGroupEntity extends BaseEntity {


    @EqualsAndHashCode.Include
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    @EqualsAndHashCode.Include
    private String name;
}
