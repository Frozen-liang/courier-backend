package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadFunctionResponse {

    private String functionKey;
    private List<String> functionParams;
    @JsonProperty("isGlobal")
    private boolean global = true;

}
