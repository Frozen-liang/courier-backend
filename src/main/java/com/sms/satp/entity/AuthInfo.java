package com.sms.satp.entity;

import com.sms.satp.engine.auth.AuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {

    private AuthType type;
    private OnePlatformAuthInfo onePlatformAuthInfo;
    private ServiceMeshAuthInfo serviceMeshAuthInfo;
}