package com.sms.courier.entity.system;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "SystemVersion")
public class SystemVersionEntity {

    private String id;
    private String version;
    private String name;
    private String group;
    private LocalDateTime buildTime;
    @Field("isInitialized")
    private boolean initialized;
    private Integer status;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
