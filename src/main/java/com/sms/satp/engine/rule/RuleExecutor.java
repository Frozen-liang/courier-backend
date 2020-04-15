package com.sms.satp.engine.rule;

import com.sms.satp.engine.model.Rule;

public interface RuleExecutor<A,E> {

    public boolean execute(Rule<A,E> rule);

    public RuleType ruleType();

}
