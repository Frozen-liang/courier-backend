package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.model.Rule;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;
import java.util.List;

public class LengthNotEqualRuleExecutor implements RuleExecutor<List<Object>, String> {

    @Override
    public boolean execute(Rule<List<Object>, String> rule) {
        return rule.getActual().size() != Integer.parseInt(rule.getExpected());
    }

    @Override
    public RuleType ruleType() {
        return RuleType.LENGTH_NOT_EQUAL;
    }
}