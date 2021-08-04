package com.sms.courier.dto.request;

import com.sms.courier.common.enums.ApiTagType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId cannot be empty.")
    private String projectId;

    private String groupId;

    private ApiTagType tagType;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The tagName cannot be empty.")
    private String tagName;
}
