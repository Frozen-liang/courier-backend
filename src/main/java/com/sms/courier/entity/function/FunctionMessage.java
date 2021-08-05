package com.sms.courier.entity.function;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionMessage {

    private List<String> ids;
    @JsonProperty("isGlobal")
    private boolean global;
    private String key;
    private Integer operationType;
}
