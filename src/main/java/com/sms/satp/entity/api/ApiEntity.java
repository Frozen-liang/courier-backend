package com.sms.satp.entity.api;

import com.sms.satp.common.enums.ApiProtocol;
import com.sms.satp.common.enums.ApiRequestParamType;
import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.RequestMethod;
import com.sms.satp.entity.api.common.HeaderInfo;
import com.sms.satp.entity.api.common.ParamInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Api")
public class ApiEntity {

    @MongoId(FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String id;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    private String apiName;

    private String apiUrl;

    private ApiProtocol apiProtocol;

    private RequestMethod requestMethod;

    private ApiRequestParamType apiRequestParamType;

    private List<HeaderInfo> headerInfo;

    private List<ParamInfo> paramInfo;

    private ApiStatus apiStatus;

    private boolean removed;

    private String preInject;

    private String postInject;

    private String swaggerId;

    @CreatedBy
    private Long createUserId;

    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime modifyTime;


}
