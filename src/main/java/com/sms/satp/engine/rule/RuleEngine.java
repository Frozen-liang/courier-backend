package com.sms.satp.engine.rule;

public interface RuleEngine {


    public RuleExecutor<?,?> getRuleExecutor(RuleType ruleType);

}
