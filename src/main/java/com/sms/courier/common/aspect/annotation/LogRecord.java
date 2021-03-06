package com.sms.courier.common.aspect.annotation;

import com.sms.courier.common.enums.OperationModule;
import com.sms.courier.common.enums.OperationType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Indexed;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface LogRecord {

    OperationType operationType();

    OperationModule operationModule();

    String refId() default "projectId";

    String template() default "";

    Enhance enhance() default @Enhance;

    String sourceId() default "";
}
