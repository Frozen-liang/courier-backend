package com.sms.satp.entity.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "页号")
    private int pageNumber = 0;
    @Builder.Default
    @ApiModelProperty(value = "页面大小")
    private int pageSize = 10;
    @Builder.Default
    @ApiModelProperty(value = "排序字段")
    private String sort = "create_date_time";
    @Builder.Default
    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order = "desc";
}