package com.sms.courier.security.oauth;

import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "AuthSetting")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AuthSettingEntity extends BaseEntity {

    private AuthType authType;
    private String authUri;
    private String clientId;
    private String clientSecret;
    private String scope;
}
