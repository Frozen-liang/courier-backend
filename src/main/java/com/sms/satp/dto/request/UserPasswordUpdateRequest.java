package com.sms.satp.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdateRequest {

    @NotBlank(message = "The id cannot be empty.")
    private String id;

    @NotBlank(message = "The oldPassword cannot be empty.")
    private String oldPassword;

    @NotBlank(message = "The oldPassword cannot be empty.")
    private String newPassword;

    @NotBlank(message = "The confirmPassword cannot be empty.")
    private String confirmPassword;
}