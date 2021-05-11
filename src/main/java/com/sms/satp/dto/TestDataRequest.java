package com.sms.satp.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TestDataRequest {

    @NotEmpty(message = "The dataName cannot be empty")
    private String dataName;
    @Valid
    @NotNull(message = "The data cannot be null")
    @Size(min = 1, message = "The data cannot be empty")
    private List<DataParamRequest> data;
}
