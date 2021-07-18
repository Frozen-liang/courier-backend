package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequest {

    @NotEmpty(message = "The id must not be empty.", groups = UpdateGroup.class)
    @Null(message = "The id must be null.", groups = InsertGroup.class)
    private String id;

    @NotEmpty(message = "The name must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String name;

    private List<String> userIds;
}