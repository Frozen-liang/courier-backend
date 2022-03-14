package com.sms.courier.common.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface TrueHandler<T> {

    void handler(Consumer<? super T> confirmed);
}
