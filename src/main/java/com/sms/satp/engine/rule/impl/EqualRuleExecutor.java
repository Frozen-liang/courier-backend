package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.model.Rule;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;

public class EqualRuleExecutor implements RuleExecutor<String, String> {


    @Override
    public boolean execute(Rule<String, String> rule) {
        return rule.getActual().equals(rule.getExpected());
    }

    @Override
    public RuleType ruleType() {
        return RuleType.EQUAL;
    }
}
