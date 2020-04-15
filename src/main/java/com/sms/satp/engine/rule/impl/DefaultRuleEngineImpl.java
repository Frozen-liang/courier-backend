package com.sms.satp.engine.rule.impl;

import com.sms.satp.engine.rule.RuleEngine;
import com.sms.satp.engine.rule.RuleExecutor;
import com.sms.satp.engine.rule.RuleType;
import java.util.ArrayList;
import java.util.List;

public class DefaultRuleEngineImpl implements RuleEngine {

    private final static List<RuleExecutor<?,?>> RULE_EXECUTORS = new ArrayList<>();

    static {
//        JsonPath.read(json, "$.store.book[*].author");
        // rule executor
//        RULE_EXECUTORS.add()
    }


    @Override
    public RuleExecutor<?,?> getRuleExecutor(RuleType ruleType) {
        return null;
    }
}
