package com.sms.satp.entity.scenetest;

import com.sms.satp.entity.BaseEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "SceneCase")
public class SceneCase extends BaseEntity {

    private String name;

    private String createUserName;

    private String projectId;

    private String groupId;

    private String testStatus;

    private List<String> caseTag;

    private Integer priority;

}
