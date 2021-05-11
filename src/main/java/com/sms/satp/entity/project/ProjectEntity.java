package com.sms.satp.entity.project;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Project")
public class ProjectEntity {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String description;
    private boolean removedd;
    @CreatedBy
    private Long createUserId;
    @LastModifiedBy
    private Long modifyUserId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;


}
