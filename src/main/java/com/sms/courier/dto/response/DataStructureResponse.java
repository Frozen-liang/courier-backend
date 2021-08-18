package com.sms.courier.dto.response;

import java.util.List;
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
public class DataStructureResponse extends BaseResponse {

    private String name;

    /**
     * 这里既有可能是projectId 也有可能是spaceId, 需要看是否是全局isGlobal字段来进行区分
     */
    private String refId;

    private boolean global;

    private List<ParamInfoResponse> struct;

    private Integer structType;

    private String description;
}
