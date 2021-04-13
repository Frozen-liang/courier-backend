package com.sms.satp.entity.env;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "GlobalEnvironment")
public class GlobalEnvironment {

    @Id
    private ObjectId id;
    private String envName;
    private String envDesc;
    private String frontUri;
    private EnvironmentAuth envAuth;
    private String beforeInject;
    private String afterInject;
    private String globalBeforeProcess;
    private String globalAfterProcess;
    private LocalDateTime createDateTime;
    private LocalDateTime modifyDateTime;
    private List<EnvironmentHeader> headers;
    private List<EnvironmentParam> params;
    private List<EnvironmentParam> urlParams;
    private List<EnvironmentParam> additionalParams;
}
