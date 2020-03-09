package com.sms.satp.entity.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseParameter {

    private String name;
    private String value;

}