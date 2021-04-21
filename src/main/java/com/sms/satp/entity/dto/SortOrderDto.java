package com.sms.satp.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortOrderDto {

    private String sceneCaseApiId;

    private String templateApiId;

    @NotNull(message = "The orderNumber can not be empty")
    private Integer orderNumber;
}
