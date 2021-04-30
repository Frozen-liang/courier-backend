package com.sms.satp.entity.api.common;

import com.sms.satp.common.enums.ParamType;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamInfo {

    private String key;
    private String value;
    private String description;
    private ParamType paramType;
    private Boolean reference;
    private Boolean required;
    private Boolean checkbox;
    @Builder.Default
    @ToString.Exclude
    private List<ParamInfo> childParam = new ArrayList<>();
}
