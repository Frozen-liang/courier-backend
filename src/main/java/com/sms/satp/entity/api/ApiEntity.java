package com.sms.satp.entity.api;

import com.sms.satp.common.enums.ApiJsonType;
import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.entity.api.common.ParamInfo;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;



@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "Api")
public class ApiEntity extends BaseEntity {


    @JsonIgnore
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;
    @Include
    @JsonIgnore
    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> tagId;
    @Include
    @Indexed(unique = true)
    private String apiName;
    @Include
    private String description;
    @Include
    private String apiPath;
    @Include
    private ApiProtocol apiProtocol;
    @Include
    private RequestMethod requestMethod;
    @Include
    private ApiRequestParamType apiRequestParamType;
    @Include
    private List<ParamInfo> requestHeaders;
    @Include
    private List<ParamInfo> responseHeaders;
    @Include
    private List<ParamInfo> pathParams;
    @Include
    private List<ParamInfo> restfulParams;
    @Include
    private List<ParamInfo> requestParams;
    @Include
    private List<ParamInfo> responseParams;
    @JsonIgnore
    private ApiStatus apiStatus;
    @JsonIgnore
    private String preInject;
    @JsonIgnore
    private String postInject;
    @Include
    private String swaggerId;

    @Include
    @JsonIgnore
    private String md5;
    @Include
    private ApiJsonType apiResponseJsonType;
    @Include
    private ApiJsonType apiRequestJsonType;


}
