package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.model.Rule;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;

public class GreaterOrEqualRuleExecutor implements RuleExecutor<String, String> {

    @Override
    public boolean execute(Rule<String, String> rule) {
        return rule.getActual().compareTo(rule.getExpected()) >= 0;
    }

    @Override
    public RuleType ruleType() {
        return RuleType.GREATER_OR_EQUAL;
    }
}