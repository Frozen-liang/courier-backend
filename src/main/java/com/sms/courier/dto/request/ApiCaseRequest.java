package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiCaseRequest {

    @NotBlank(message = "The id must not be empty.")
    private String id;
    @NotBlank(message = "The caseName must not be empty.")
    private String caseName;
    @NotBlank(message = "The createUsername must not be empty.")
    private String createUsername;
    @NotBlank(message = "The apiPath must not be empty.")
    private String apiPath;
    @NotNull(message = "The requestMethod must not be null.")
    private Integer requestMethod;
}