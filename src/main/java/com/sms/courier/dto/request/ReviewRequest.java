package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotBlank(message = "The projectId must not be empty.")
    private String projectId;

    @NotBlank(message = "The sceneCaseId must not be empty.")
    private String sceneCaseId;

    @NotBlank(message = "The sceneCaseName must not be empty.")
    private String sceneCaseName;

    @NotNull(message = "The reviewStatus must not be empty.")
    private Integer reviewStatus;

    private String comment;
}
