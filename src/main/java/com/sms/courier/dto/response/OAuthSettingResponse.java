package com.sms.courier.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class OAuthSettingResponse extends BaseResponse {

    private String url;
    private String name;
    private String icon;
    private String authPath;
    private String tokenPath;
    private String userInfoPath;
    private String clientId;
    private String scope;
    private String emailKey;
    private String usernameKey;


}
