package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.security.oauth.OAuthType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthSettingRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The authType cannot be null.")
    private OAuthType authType;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The authUri cannot be empty.")
    private String authUri;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The clientId cannot be empty.")
    private String clientId;

    @NotBlank(groups = {InsertGroup.class}, message = "The clientSecret cannot be empty.")
    @Exclude
    private String clientSecret;

    private String scope;

}
