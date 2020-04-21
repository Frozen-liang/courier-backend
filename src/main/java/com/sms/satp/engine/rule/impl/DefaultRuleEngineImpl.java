package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.rule.RuleEngine;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DefaultRuleEngineImpl implements RuleEngine {

    private static final Map<RuleType, RuleExecutor<?,?>> RULE_EXECUTORS =
        new EnumMap<>(RuleType.class);

    static {
        RULE_EXECUTORS.put(RuleType.NOT_CHECK, new NotCheckRuleExecutor());
        RULE_EXECUTORS.put(RuleType.CONTAIN, new ContainRuleExecutor());
        RULE_EXECUTORS.put(RuleType.EQUAL, new EqualRuleExecutor());
        RULE_EXECUTORS.put(RuleType.GREATER, new GreaterRuleExecutor());
        RULE_EXECUTORS.put(RuleType.GREATER_OR_EQUAL, new GreaterOrEqualRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LENGTH_EQUAL, new LengthEqualRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LENGTH_GREATER, new LengthGreaterRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LENGTH_LESS, new LengthLessRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LENGTH_NOT_EQUAL, new LengthNotEqualRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LESS, new LessRuleExecutor());
        RULE_EXECUTORS.put(RuleType.LESS_OR_EQUAL, new LessOrEqualRuleExecutor());
        RULE_EXECUTORS.put(RuleType.NOT_EQUAL, new NotEqualRuleExecutor());
    }


    @Override
    public RuleExecutor<?,?> getRuleExecutor(RuleType ruleType) {
        return RULE_EXECUTORS.get(ruleType);
    }
}
