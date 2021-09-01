package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetByEmailRequest {

    @NotBlank(message = "Email must not be empty!")
    private String email;
    @NotBlank(message = "Password must not be empty!")
    private String password;
    @NotBlank(message = "Code must not be empty!")
    private String code;
}