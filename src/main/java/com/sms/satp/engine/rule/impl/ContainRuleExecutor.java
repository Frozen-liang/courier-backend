package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.model.Rule;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;
import java.util.List;

public class ContainRuleExecutor implements RuleExecutor<List<String>, String> {

    @Override
    public boolean execute(Rule<List<String>, String> rule) {
        return rule.getActual().contains(rule.getExpected());
    }

    @Override
    public RuleType ruleType() {
        return RuleType.CONTAIN;
    }
}