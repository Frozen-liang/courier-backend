package com.sms.courier.utils;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.mongodb.internal.validator.CollectibleDocumentFieldNameValidator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;

public class IgnoreMongoCheck {

    public static void run() {
        ByteBuddyAgent.install();
        new ByteBuddy()
            .redefine(CollectibleDocumentFieldNameValidator.class)
            .method(named("validate"))
            .intercept(FixedValue.value(true))
            .make()
            .load(CollectibleDocumentFieldNameValidator.class.getClassLoader(),
                ClassReloadingStrategy.fromInstalledAgent());
    }
}
