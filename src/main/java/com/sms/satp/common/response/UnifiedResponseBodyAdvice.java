package com.sms.satp.common.response;

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

@RestControllerAdvice("com.sms.satp.controller")
public class UnifiedResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
        @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.hasMethodAnnotation(IgnoreWrap.class) && Objects.nonNull(returnType.getMethod())
            && Objects.nonNull(returnType.getMethod().getReturnType()) && returnType.getMethod().getReturnType()
            .isAssignableFrom(Void.TYPE);
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(Object body, @Nullable MethodParameter returnType,
        @Nullable MediaType selectedContentType,
        @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType, @Nullable ServerHttpRequest request,
        @Nullable ServerHttpResponse response) {
        return Response.ok(body);
    }
}
