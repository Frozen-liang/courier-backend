package com.sms.satp.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DataParamRequest {

    @NotEmpty(message = "The key cannot by empty")
    private String key;
    private Object value;
}
