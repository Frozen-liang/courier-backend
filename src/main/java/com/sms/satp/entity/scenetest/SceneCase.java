package com.sms.satp.entity.scenetest;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SceneCase")
public class SceneCase {

    @MongoId(value = FieldType.OBJECT_ID)
    private ObjectId id;

    private String name;

    private String createUserName;

    private String projectId;

    private String groupId;

    private String testStatus;

    private List<String> caseTag;

    private Integer priority;

    private Integer status;

    private String createUserId;

    private LocalDateTime createDateTime;

    private String modifyUserId;

    private LocalDateTime modifyDateTime;
}
