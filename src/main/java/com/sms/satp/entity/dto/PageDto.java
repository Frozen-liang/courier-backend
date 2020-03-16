package com.sms.satp.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    @Builder.Default
    @Schema(title = "Page Number")
    private int pageNumber = 1;
    @Builder.Default
    @Schema(title = "Page Size")
    private int pageSize = 10;
    @Builder.Default
    @Schema(title = "Sorted fields")
    private String sort = "create_date_time";
    @Builder.Default
    @Schema(title = "Way of sorting(asc/desc)")
    private String order = "desc";
}