package com.sms.courier.entity.workspace;

import com.sms.courier.entity.BaseEntity;
import java.util.List;
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
@Document(collection = "Workspace")
public class WorkspaceEntity extends BaseEntity {

    private String name;

    private Integer limit;

    private String createUsername;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> userIds;
}
