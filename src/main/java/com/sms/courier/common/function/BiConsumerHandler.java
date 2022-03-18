package com.sms.courier.common.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface BiConsumerHandler<T, U> {

    void handler(BiConsumer<T, U> confirmed);

}
