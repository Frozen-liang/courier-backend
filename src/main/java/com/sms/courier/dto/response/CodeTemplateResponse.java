package com.sms.courier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplateResponse {

    private String name;

    private String value;

    private Integer templateType;

}
