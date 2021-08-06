package com.sms.courier.common.response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice("com.sms.courier.controller")
public class UnifiedResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public boolean supports(@NonNull MethodParameter returnType,
        @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.hasMethodAnnotation(IgnoreWrap.class) && !Objects.requireNonNull(returnType.getMethod())
            .getReturnType()
            .isAssignableFrom(Void.TYPE);
    }

    @Override
    @NonNull
    public Object beforeBodyWrite(Object body, @Nullable MethodParameter returnType,
        @Nullable MediaType selectedContentType,
        @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType, @Nullable ServerHttpRequest request,
        @Nullable ServerHttpResponse response) {

        return Response.ok(body);
    }
}
