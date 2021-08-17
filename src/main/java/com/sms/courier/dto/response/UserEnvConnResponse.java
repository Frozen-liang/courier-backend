package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEnvConnResponse {

    private String id;
    private String projectId;
    private String envId;
}
