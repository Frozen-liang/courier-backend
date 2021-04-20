package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ParamType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamInfo {

    private String key;
    private String value;
    private String description;
    private ParamType paramType;
    private boolean required;
    private List<ParamInfo> childParam;
}
