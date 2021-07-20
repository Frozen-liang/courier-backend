package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GlobalFunctionRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The functionKey cannot be empty")
    private String functionKey;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The workspaceId cannot be empty")
    private String workspaceId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The functionName cannot be empty")
    private String functionName;

    @Valid
    private List<ParamInfoRequest> functionParams;


    private String functionCode;
}
