package com.sms.courier.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@Data
public class JwtAuth {

    private String typ;
    private String alg;
    private String payload;
    private String secretSalt;
    private String position;
    private String tokenName;
    @Field("isNeedBearer")
    private boolean needBearer;
}
