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
public class AuthSettingResponse extends BaseResponse {

    private Integer authType;

    private String authUri;

    private String clientId;

    private String clientSecret;

    private String scope;


}
