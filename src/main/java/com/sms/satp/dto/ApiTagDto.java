package com.sms.satp.dto;

import com.sms.satp.common.enums.ApiTagType;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagDto {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty")
    private String projectId;
    private String groupId;
    @NotEmpty(message = "The tagName cannot be empty")
    private String tagName;
    private ApiTagType tagType;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;

}
