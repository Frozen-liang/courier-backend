package com.sms.satp.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JwtAuth {

    private String typ;
    private String alg;
    private String payload;
    private String secretSalt;
    private String position;
    private String tokenName;
    private boolean bearer;
}
