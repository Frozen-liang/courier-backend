package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class DataStructureListResponse extends BaseResponse {

    private String name;

    private String refId;

    private boolean global;

    private Integer structType;

    private String description;

    @JsonProperty("isQuoted")
    private boolean quoted;

}
