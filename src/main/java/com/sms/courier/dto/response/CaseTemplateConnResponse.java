package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTemplateConnResponse extends LookupUserResponse {

    private String name;
    private String description;
    private String projectId;
    private String groupId;
    private String groupName;
    private String testStatus;
    private Integer priority;
    private List<String> tagId;
    private List<String> tagName;
    @Field("isNext")
    @JsonProperty("isNext")
    private boolean next;
    private List<EnvDataCollConnResponse> envDataCollConnList;
}
