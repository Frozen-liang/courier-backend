package com.sms.courier.dto.request;

import com.sms.courier.common.enums.ApiRequestParamType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class DataStructureRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(message = "The name must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String name;

    /**
     * 这里既有可能是projectId 也有可能是spaceId, 需要看是否是全局isGlobal字段来进行区分
     */
    @NotBlank(message = "The refId must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private String refId;

    private boolean global;

    @Valid
    @NotNull(message = "The struct must not bu null.", groups = {UpdateGroup.class, InsertGroup.class})
    @Size(min = 1, message = "The struct must not bu empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private List<ParamInfoRequest> struct;

    @NotNull(message = "The structType must not be empty.", groups = {UpdateGroup.class, InsertGroup.class})
    private ApiRequestParamType structType;

    private String description;
}
