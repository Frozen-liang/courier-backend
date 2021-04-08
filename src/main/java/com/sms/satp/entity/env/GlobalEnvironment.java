package com.sms.satp.entity.env;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "GlobalEnvironment")
public class GlobalEnvironment {

    @Id
    @Field("_id")
    private String id;
    @Field("env_name")
    private String envName;
    @Field("env_desc")
    private String envDesc;
    @Field("front_uri")
    private String frontUri;
    @Field("env_auth")
    private EnvironmentAuth envAuth;
    @Field("before_inject")
    private String beforeInject;
    @Field("after_inject")
    private String afterInject;
    @Field("global_before_process")
    private String globalBeforeProcess;
    @Field("global_after_process")
    private String globalAfterProcess;
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;
    @Field("header")
    private List<EnvironmentHeader> headers;
    @Field("param")
    private List<EnvironmentParam> params;
    @Field("url_param")
    private List<EnvironmentParam> urlParams;
    @Field("additional_param")
    private List<EnvironmentParam> additionalParams;
}
