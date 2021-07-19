package com.sms.satp.entity.function;

import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.api.common.ParamInfo;
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
@Document(collection = "ProjectFunction")
public class ProjectFunctionEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private String functionKey;
    private String functionName;
    private List<ParamInfo> functionParams;
    private String functionCode;
}
