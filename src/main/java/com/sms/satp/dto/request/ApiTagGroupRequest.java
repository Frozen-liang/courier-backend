package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagGroupRequest {

    @NotEmpty(groups = {UpdateGroup.class}, message = "The id must not be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotEmpty(groups = {UpdateGroup.class, InsertGroup.class}, message = "The projectId must not be empty.")
    private String projectId;

    @NotEmpty(groups = {UpdateGroup.class, InsertGroup.class}, message = "The name must not be empty.")
    private String name;
}
