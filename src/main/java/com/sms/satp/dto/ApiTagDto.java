package com.sms.satp.dto;

import com.sms.satp.common.enums.ApiTagType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagDto {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty.")
    private String projectId;
    private String groupId;
    @NotEmpty(message = "The tagName cannot be empty.")
    private String tagName;
    @NotNull(message = "The tagType cannot by null.")
    @Range(min = 1, max = 3, message = "The tayType must between 1 and 3.")
    private Integer tagType;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;

}
