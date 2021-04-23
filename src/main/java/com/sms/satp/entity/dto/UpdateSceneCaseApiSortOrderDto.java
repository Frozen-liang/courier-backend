package com.sms.satp.entity.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSceneCaseApiSortOrderDto {

    @NotEmpty(message = "The sortOrderDtoList can not be empty")
    private List<SortOrderDto> sortOrderDtoList;

    @NotNull(message = "The sceneCaseId can not be empty")
    private String sceneCaseId;

}
