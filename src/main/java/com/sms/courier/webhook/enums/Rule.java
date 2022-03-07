package com.sms.courier.webhook.enums;

import com.sms.courier.common.enums.EnumCommon;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Rule implements EnumCommon {
    LESS_THAN(1, (e1, e2) -> e1 < e2),
    GREATER_THAN(2, (e1, e2) -> e1 > e2),
    LESS_THAN_OR_EQUAL(3, (e1, e2) -> e1 <= e2),
    GREATER_THAN_OR_EQUAL(4, (e1, e2) -> e1 >= e2);

    private final int code;
    private final BiPredicate<Double, Double> predicate;
    private static final Map<Integer, Rule> MAPPINGS = Arrays.stream(values()).sequential().collect(
        Collectors.toMap(Rule::getCode, Function.identity()));


    Rule(int code, BiPredicate<Double, Double> predicate) {
        this.code = code;
        this.predicate = predicate;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public static Rule getRule(Integer code) {
        return MAPPINGS.get(code);
    }

    public BiPredicate<Double, Double> getPredicate() {
        return predicate;
    }
}
