package com.sms.satp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    private String id;
    private String name;
    private String description;
    private Boolean required;
}
