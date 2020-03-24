package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMeshAuthInfo {

    private String formAction;
    private String userName;
    private String password;
    private Boolean loggingEnabled;

}