package com.sms.satp.entity.system;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "SystemVersion")
public class SystemVersion {

    private String id;
    private String version;
    @Field("isInitialized")
    private Boolean initialized;
    private Integer status;
}
