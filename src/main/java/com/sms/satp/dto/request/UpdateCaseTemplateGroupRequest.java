package com.sms.satp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCaseTemplateGroupRequest {

    @NotNull(message = "The id can not be empty")
    private String id;

    private String projectId;

    private String name;

    private String parentId;

    @JsonProperty("isRemoved")
    private boolean removed;
}
