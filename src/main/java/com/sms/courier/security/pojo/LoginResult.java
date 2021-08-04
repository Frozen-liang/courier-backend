package com.sms.courier.security.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResult {

    private String token;

}
