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
public class DataParamDto {

    @NotEmpty(message = "The paramKey cannot by empty")
    private String paramKey;
    private Object paramValue;
}
