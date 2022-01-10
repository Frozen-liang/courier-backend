package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The icon cannot be empty.")
    private String icon;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name cannot be empty.")
    private String name;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The url cannot be empty.")
    private String url;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The authPath cannot be empty.")
    private String authPath;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The tokenPath cannot be empty.")
    private String tokenPath;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The userInfoPath cannot be empty.")
    private String userInfoPath;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The clientId cannot be empty.")
    private String clientId;

    @NotBlank(groups = {InsertGroup.class}, message = "The clientSecret cannot be empty.")
    @Exclude
    private String clientSecret;

    private String scope;

    private String emailKey = "email";

    private String usernameKey = "username";

}
