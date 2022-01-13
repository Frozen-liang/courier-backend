package com.sms.courier.security.oauth;

import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "OAuthSetting")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OAuthSettingEntity extends BaseEntity {

    private String url;
    private String name;
    private String icon;
    private String authPath;
    private String tokenPath;
    private String userInfoPath;
    private String clientId;
    private String clientSecret;
    private String scope;
    @Default
    private String emailKey = "email";
    @Default
    private String usernameKey = "username";
}
