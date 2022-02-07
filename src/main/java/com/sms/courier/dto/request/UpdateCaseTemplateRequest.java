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
public class UpdateCaseTemplateRequest {

    @NotNull(message = "The id can not be empty")
    private String id;
    @NotNull(message = "The name can not be empty")
    private String name;
    private String description;
    @NotNull(message = "The projectId can not be empty")
    private String projectId;
    private String groupId;
    private String testStatus;
    private List<String> tagId;
    private Integer priority;
    @JsonProperty("isNext")
    private boolean next;
    @NotNull(message = "The envDataCollConnList can not be empty")
    private List<EnvDataCollConnRequest> envDataCollConnList;
}
