package com.sms.satp.entity.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiLabelDto {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty")
    private String projectId;
    private String groupId;
    @NotEmpty(message = "The labelName cannot be empty")
    private String labelName;
    /*1:apiLabel 2:apiCaseLabel 3:apiCasePipelineLabel*/
    @Min(value = 1, message = "The labelType between 1 and 3")
    @Max(value = 3, message = "The labelType between 1 and 3")
    private Short labelType;
    private String createDateTime;
    private String modifyDateTime;

}