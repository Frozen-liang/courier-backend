package com.sms.satp.entity.system;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "SystemVersion")
public class SystemVersion {

    private String id;
    private String version;
    private String name;
    private String group;
    private LocalDateTime buildTime;
    private Boolean initialized;
    private Integer status;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
