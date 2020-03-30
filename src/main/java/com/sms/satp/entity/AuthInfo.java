package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {

    private String type;
    private OnePlatformAuthInfo onePlatformAuthInfo;
    private ServiceMeshAuthInfo serviceMeshAuthInfo;
}