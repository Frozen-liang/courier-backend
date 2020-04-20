package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.model.Rule;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;

public class LessRuleExecutor implements RuleExecutor<String, String> {

    @Override
    public boolean execute(Rule<String, String> rule) {
        return rule.getActual().compareTo(rule.getExpected()) < 0;
    }

    @Override
    public RuleType ruleType() {
        return RuleType.LESS;
    }
}