package com.sms.courier.engine.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineRegistrationRequest {

    @NotBlank(message = "The name must not be null.")
    private String name;
    private String host;
    private String version = "1.0.0";
}
