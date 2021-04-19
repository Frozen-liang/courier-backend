package com.sms.satp.entity.dto;

import com.sms.satp.common.enums.ApiLabelType;
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
    private ApiLabelType labelType;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;

}
