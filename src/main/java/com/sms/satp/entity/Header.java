package com.sms.satp.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {

    private String id;
    private String name;
    private String description;
    private Boolean required;
}
