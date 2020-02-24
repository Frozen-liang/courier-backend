package com.sms.satp.entity;

import com.sms.satp.engine.auth.OnePlatformAuthConfig;
import com.sms.satp.engine.auth.ServiceMeshAuthConfig;

public class AuthInfo {

    private String type;
    private OnePlatformAuthConfig onePlatformAuthConfig;
    private ServiceMeshAuthConfig serviceMeshAuthConfig;
}