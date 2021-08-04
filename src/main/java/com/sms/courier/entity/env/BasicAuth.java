package com.sms.courier.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicAuth {

    private String username;
    private String password;
}
