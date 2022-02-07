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
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseResponse extends LookupUserResponse {

    private String name;
    private String description;
    private String projectId;
    private String groupName;
    private String groupId;
    private String testStatus;
    private List<String> tagName;
    private List<String> tagId;
    private Integer priority;
    @Field("isNext")
    @JsonProperty("isNext")
    private boolean next;
    private List<EnvDataCollResponse> envDataCollConnList;
}
