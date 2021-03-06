package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCaseTemplateRequest {

    @NotNull(message = "The name can not be empty")
    private String name;
    private String description;
    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    @NotNull(message = "The groupId can not be empty")
    private String groupId;
    private String testStatus;
    private List<String> tagId;
    private Integer priority;
    @JsonProperty("isNext")
    private boolean next;
    private List<EnvDataCollConnRequest> envDataCollConnList;
}
