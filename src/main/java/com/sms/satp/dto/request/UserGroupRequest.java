package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRequest {

    @NotEmpty(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name must not be empty.")
    private String name;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The roleIds must not be empty.")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "The roleIds must not be empty.")
    private List<String> roleIds;
}