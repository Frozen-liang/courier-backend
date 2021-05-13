package com.sms.satp.entity.env;

import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.api.common.HeaderInfo;
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
@Document(collection = "ProjectEnvironment")
public class ProjectEnvironment extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private String envName;
    private String envDesc;
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private List<HeaderInfo> headers;
    private List<ParamInfo> params;
    private List<ParamInfo> urlParams;
    private List<ParamInfo> additionalParams;
}
