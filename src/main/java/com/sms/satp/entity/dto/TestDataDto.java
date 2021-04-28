package com.sms.satp.entity.dto;

import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestDataDto {

    @NotEmpty(message = "The dataName cannot be empty")
    private String dataName;
    @NotNull(message = "The data cannot be null")
    private Map<String,Object> data;
}
