package com.sms.satp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCodeDocDto {

    private String id;
    private String code;
    private String description;
    private String projectId;
    private String createDateTime;
    private String modifyDateTime;

}