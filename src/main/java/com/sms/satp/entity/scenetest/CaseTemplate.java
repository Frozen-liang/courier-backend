package com.sms.satp.entity.scenetest;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "CaseTemplate")
public class CaseTemplate {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    private String name;

    private String createUserName;

    private String projectId;

    private String groupId;

    private String testStatus;

    private List<String> caseTag;

    private boolean remove;
    @CreatedBy
    private Long createUserId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedBy
    private Long modifyUserId;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
