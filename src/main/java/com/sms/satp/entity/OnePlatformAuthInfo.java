package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnePlatformAuthInfo {

    private String formAction;
    private String userNameInputField;
    private String passwordInputField;
    private String userName;
    private String password;
    private String utcOffset;
    private Boolean supportDst;
    private Boolean loggingEnabled;

}