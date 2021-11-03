package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerSettingRequest {

    @NotBlank(message = "The id must not be empty!")
    private String id;
    @NotBlank(message = "The netWorkId must not be empty!")
    private String netWorkId;
    private String username;
    private String password;
    private String registryAddress;
}
