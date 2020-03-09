package com.sms.satp.entity.test;

import com.sms.satp.parser.common.SchemaType;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchRule {

    private String paramKey;
    private SchemaType paramType;
    private String expectValue;
    private Boolean checkExist;
    private Boolean checkParamType;
    private Boolean checkValue;
    private Map<String, MatchRule> properties;

}