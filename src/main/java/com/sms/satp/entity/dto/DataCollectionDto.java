package com.sms.satp.entity.dto;

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
@Data
@Builder
public class DataCollectionDto {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty")
    private String projectId;
    @NotEmpty(message = "The collectionName cannot be empty")
    private String collectionName;
    @NotNull(message = "The paramList cannot be null")
    @Size(min = 1, message = "The paramList cannot be empty")
    private List<String> paramList;
    @Valid
    @NotNull(message = "The dataList cannot be null")
    @Size(min = 1, message = "The dataList cannot be empty")
    private List<TestDataDto> dataList;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
