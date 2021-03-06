package com.sms.courier.common.aspect.annotation;

public @interface Enhance {

    boolean enable() default false;

    String primaryKey() default "id";

    String queryResultKey() default "result";
}
