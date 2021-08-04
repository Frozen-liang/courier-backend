package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTemplateResponse extends LookupUserResponse {

    private String name;
    private String projectId;
    private String groupId;
    private String dataCollId;
    private String groupName;
    private String testStatus;
    private Integer priority;
    private List<String> tagId;
    private List<String> tagName;
    @JsonProperty("isNext")
    private boolean next;
}
