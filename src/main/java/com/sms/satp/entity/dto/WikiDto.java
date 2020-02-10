package com.sms.satp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikiDto {

    private String id;
    private String title;
    private Integer contentType;
    private String content;
    private String projectId;
    private String createDateTime;
    private String modifyDateTime;
}