package com.sms.courier.entity.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplate {

    private String name;

    private String vaule;

    private String templateType;

    private String description;
}
