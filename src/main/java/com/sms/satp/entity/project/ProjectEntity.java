package com.sms.satp.entity.project;

import static org.springframework.data.mongodb.core.mapping.FieldType.OBJECT_ID;

import com.sms.satp.common.enums.ProjectType;
import com.sms.satp.entity.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "Project")
public class ProjectEntity extends BaseEntity {

    private String name;
    @Field(targetType = OBJECT_ID)
    private String workspaceId;
    private String description;
    private String version;
    private ProjectType type;
}
