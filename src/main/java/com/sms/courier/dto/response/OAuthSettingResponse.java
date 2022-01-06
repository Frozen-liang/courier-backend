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

    private Integer authType;

    private String icon;

    private String authUri;

    private String tokenUri;

    private String userInfoUri;

    private String clientId;

    private String scope;


}
