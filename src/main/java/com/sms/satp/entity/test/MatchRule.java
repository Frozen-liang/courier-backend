package com.sms.satp.entity.test;

import com.sms.satp.engine.rule.RuleType;
import com.sms.satp.parser.common.SchemaType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchRule {

    private String paramKey;
    private SchemaType paramType;
    private String paramInfo;
    private Boolean checkExist;
    private Boolean checkParamType;
    private RuleType ruleType;
    private List<MatchRule> childList;

}