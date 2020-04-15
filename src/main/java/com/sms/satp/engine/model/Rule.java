package com.sms.satp.engine.model;

public class Rule<A, E> {

    private final A actual;
    private final E expected;

    public Rule(A actual, E expected) {
        this.actual = actual;
        this.expected = expected;
    }

    public A getActual() {
        return actual;
    }

    public E getExpected() {
        return expected;
    }
}
