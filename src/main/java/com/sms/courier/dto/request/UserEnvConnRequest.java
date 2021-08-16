package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEnvConnRequest {

    private String id;
    @NotBlank(message = "The projectId must not be empty.")
    private String projectId;
    @NotBlank(message = "The envId must not be empty.")
    private String envId;

}
