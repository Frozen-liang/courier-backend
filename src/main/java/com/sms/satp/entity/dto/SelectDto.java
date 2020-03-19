package com.sms.satp.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectDto {

    private String id;
    private String name;

}