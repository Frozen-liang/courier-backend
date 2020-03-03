package com.sms.satp.entity;

import com.sms.satp.engine.auth.OnePlatformAuthConfig;
import com.sms.satp.engine.auth.ServiceMeshAuthConfig;
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
    private OnePlatformAuthConfig onePlatformAuthConfig;
    private ServiceMeshAuthConfig serviceMeshAuthConfig;
}