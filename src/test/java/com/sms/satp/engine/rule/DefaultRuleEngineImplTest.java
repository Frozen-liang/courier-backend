package com.sms.satp.engine.rule;

import com.sms.satp.ApplicationTests;
import com.sms.satp.engine.model.Rule;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultRuleEngineImplTest {

    @Autowired
    private RuleEngine ruleEngine;

    @Test
    @DisplayName("Test EqualRuleExecutor")
    void equal_test() {
        Rule<String, String> rule = new Rule<>("30", "20");
        RuleExecutor<String, String> ruleExecutor = (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(false, result);
    }

    @Test
    @DisplayName("Test EqualRuleExecutor")
    void not_equal_test() {
        Rule<String, String> rule = new Rule<>("30", "20");
        RuleExecutor<String, String> ruleExecutor =
            (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.NOT_EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }

    @Test
    @DisplayName("Test GreaterRuleExecutor")
    void greater_test() {
        Rule<String, String> rule = new Rule<>("30", "20");
        RuleExecutor<String, String> ruleExecutor =
            (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.GREATER);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }

    @Test
    @DisplayName("Test GreaterOrEqualRuleExecutor")
    void greater_or_equal_test() {
        Rule<String, String> rule = new Rule<>("20", "20");
        RuleExecutor<String, String> ruleExecutor =
            (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.GREATER_OR_EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }

    @Test
    @DisplayName("Test LengthEqualRuleExecutor")
    void length_equal_test() {
        List<Object> stringList = Arrays.asList("a", "b", "c");
        Rule<List<Object>, String> rule = new Rule<>(stringList, "3");
        RuleExecutor<List<Object>, String> ruleExecutor =
            (RuleExecutor<List<Object>, String>) ruleEngine.getRuleExecutor(RuleType.LENGTH_EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }

    @Test
    @DisplayName("Test LengthGreaterRuleExecutor")
    void length_greater_test() {
        List<Object> stringList = Arrays.asList("a", "b", "c");
        Rule<List<Object>, String> rule = new Rule<>(stringList, "3");
        RuleExecutor<List<Object>, String> ruleExecutor =
            (RuleExecutor<List<Object>, String>) ruleEngine.getRuleExecutor(RuleType.LENGTH_GREATER);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(false, result);
    }

    @Test
    @DisplayName("Test LengthLessRuleExecutor")
    void length_less_test() {
        List<Object> stringList = Arrays.asList("a", "b", "c");
        Rule<List<Object>, String> rule = new Rule<>(stringList, "3");
        RuleExecutor<List<Object>, String> ruleExecutor =
            (RuleExecutor<List<Object>, String>) ruleEngine.getRuleExecutor(RuleType.LENGTH_LESS);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(false, result);
    }

    @Test
    @DisplayName("Test LengthNotEqualRuleExecutor")
    void length_not_equal_test() {
        List<Object> stringList = Arrays.asList("a", "b", "c");
        Rule<List<Object>, String> rule = new Rule<>(stringList, "3");
        RuleExecutor<List<Object>, String> ruleExecutor =
            (RuleExecutor<List<Object>, String>) ruleEngine.getRuleExecutor(RuleType.LENGTH_NOT_EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(false, result);
    }

    @Test
    @DisplayName("Test LessRuleExecutor")
    void less_test() {
        Rule<String, String> rule = new Rule<>("30", "20");
        RuleExecutor<String, String> ruleExecutor =
            (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.LESS);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(false, result);
    }

    @Test
    @DisplayName("Test LessOrEqualRuleExecutor")
    void less_or_equal_test() {
        Rule<String, String> rule = new Rule<>("20", "20");
        RuleExecutor<String, String> ruleExecutor =
            (RuleExecutor<String, String>) ruleEngine.getRuleExecutor(RuleType.LESS_OR_EQUAL);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }

    @Test
    @DisplayName("Test ContainRuleExecutor")
    void contain_test() {
        List<String> stringList = Arrays.asList("a", "b", "c");
        Rule<List<String>, String> rule = new Rule<>(stringList, "a");
        RuleExecutor<List<String>, String> ruleExecutor =
            (RuleExecutor<List<String>, String>) ruleEngine.getRuleExecutor(RuleType.CONTAIN);
        Boolean result = ruleExecutor.execute(rule);
        Assertions.assertEquals(true, result);
    }


}