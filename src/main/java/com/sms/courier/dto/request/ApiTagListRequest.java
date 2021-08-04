package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiTagListRequest {

    @NotBlank(message = "The projectId cannot be empty.")
    private String projectId;

    private String groupId;

    private Integer tagType;

    private String tagName;
}
