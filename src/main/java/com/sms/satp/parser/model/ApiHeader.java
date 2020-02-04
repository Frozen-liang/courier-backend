package com.sms.satp.parser.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiHeader {
    private String name;
    private String description;
}
