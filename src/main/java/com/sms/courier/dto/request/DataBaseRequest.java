package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id can not be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId can not be empty.")
    private String projectId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name can not be empty.")
    private String name;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The dataBaseType can not be empty.")
    private Integer databaseType;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The url can not be empty.")
    private String url;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The port can not be empty.")
    private String port;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The username can not be empty.")
    private String username;

    @NotBlank(groups = {InsertGroup.class}, message = "The password can not be empty.")
    private String password;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The dataBaseName can not be empty.")
    private String dataBaseName;

}
