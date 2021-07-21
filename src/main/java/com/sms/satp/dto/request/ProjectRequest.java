package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ProjectType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @NotBlank(message = "The id must not be empty.", groups = UpdateGroup.class)
    @Null(message = "The id must be null.", groups = InsertGroup.class)
    private String id;
    @NotBlank(message = "The workspaceId must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String workspaceId;
    @NotBlank(message = "The name must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String name;
    @NotBlank(message = "The description must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String description;
    private String version;
    private ProjectType type;

}