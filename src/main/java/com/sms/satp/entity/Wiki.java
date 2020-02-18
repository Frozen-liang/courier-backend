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
public class Wiki {

    @Id
    @Field("_id")
    private String id;
    private String title;
    @Field("content_type")
    private Integer contentType;
    private String content;
    private String projectId;
    @Field("create_date_time")
    private LocalDateTime createDateTime;
    @Field("modify_date_time")
    private LocalDateTime modifyDateTime;
}