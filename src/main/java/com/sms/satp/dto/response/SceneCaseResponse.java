package com.sms.satp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseResponse extends BaseResponse {

    private String name;
    private String createUserName;
    private String projectId;
    private String dataCollId;
    private String groupName;
    private String groupId;
    private String testStatus;
    private List<String> tagName;
    private List<String> tagId;
    private Integer priority;
    @JsonProperty("isLock")
    private boolean lock;
}
