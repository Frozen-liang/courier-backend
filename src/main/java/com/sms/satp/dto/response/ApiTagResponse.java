package com.sms.satp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiTagResponse extends BaseResponse {

    private String projectId;
    private String groupId;
    private String tagName;
    private Integer tagType;

}
