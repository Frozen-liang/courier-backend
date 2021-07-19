package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = InsertGroup.class, message = "The password cannot be empty.")
    private String password;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The username cannot be empty.")
    private String username;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The groupId cannot be empty.")
    private String groupId;

    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "The email cannot be empty.")
    @Email(groups = {UpdateGroup.class, InsertGroup.class}, message = "The email format is incorrect.")
    private String email;

    @Default
    private Boolean enabled = true;
}