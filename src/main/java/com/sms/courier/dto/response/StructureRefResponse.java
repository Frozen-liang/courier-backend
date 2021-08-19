package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureRefResponse {

    private String id;

    @JsonProperty("isGlobal")
    private boolean global;

    private List<ParamInfoResponse> struct;

    private Integer structType;
}
