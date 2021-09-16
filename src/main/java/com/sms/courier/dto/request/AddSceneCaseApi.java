package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSceneCaseApi {

    private String id;

    private Integer order;

    @JsonProperty("isCase")
    private boolean isCase;

    private String name;

}
