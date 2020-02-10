package com.sms.satp.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "StatusCodeDoc")
public class StatusCodeDoc {

    @Id
    private String id;
    private String code;
    private String description;
    private String projectId;
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;
}