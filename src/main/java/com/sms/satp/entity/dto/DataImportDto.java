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
public class DataImportDto {

    @NotNull(message = "Import url cannot be empty")
    private String url;
    @NotNull(message = "Data type cannot be empty")
    private String type;
    @NotNull(message = "ProjectId cannot be empty")
    private String projectId;

}