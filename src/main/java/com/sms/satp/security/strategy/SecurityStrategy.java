package com.sms.satp.security.strategy;

import com.sms.satp.security.TokenType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
@Component
public @interface SecurityStrategy {

    TokenType type();
}
