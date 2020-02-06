package com.sms.satp.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document
public class ProjectEnvironment {

    @Id
    private String id;
    private String name;
    private String desc;
    @Field("project_id")
    private String projectId;
    @Field("base_path")
    private String basePath;

}