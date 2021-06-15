package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiTagType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagRequest {

    @NotEmpty(groups = UpdateGroup.class, message = "The id cannot be empty.")
    private String id;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId cannot be empty.")
    private String projectId;

    private String groupId;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The tagName cannot be empty.")
    private String tagName;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The tagType cannot by null.")
    private ApiTagType tagType;
}
