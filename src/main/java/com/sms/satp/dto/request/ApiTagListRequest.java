package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiTagType;
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
public class ApiTagListRequest {

    @NotEmpty(message = "The projectId cannot be empty.")
    private String projectId;

    private String groupId;

    private Integer tagType;

    private String tagName;
}
