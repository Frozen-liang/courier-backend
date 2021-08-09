package com.sms.courier.entity.env;

import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.entity.BaseEntity;
import com.sms.courier.entity.api.common.ParamInfo;
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

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "GlobalEnvironment")
public class GlobalEnvironmentEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String workspaceId;
    private String envName;
    private String envDesc;
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private List<ParamInfo> headers;
    private List<ParamInfo> envVariable;
    private List<ParamInfo> urlParams;
    private ApiRequestParamType requestParamType;
    private List<ParamInfo> requestParams;
}
