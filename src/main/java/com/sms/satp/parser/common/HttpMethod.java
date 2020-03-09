package com.sms.satp.parser.common;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.ErrorCode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSender;
import io.vavr.CheckedFunction2;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;

public enum HttpMethod {


    GET(HttpMethod::get), POST(HttpMethod::post), PUT(HttpMethod::put), PATCH(
        HttpMethod::patch), DELETE(HttpMethod::delete), HEAD(HttpMethod::unSupport), OPTIONS(
        HttpMethod::unSupport), TRACE(HttpMethod::unSupport);


    private final CheckedFunction2<String, RequestSender, Response> executor;

    HttpMethod(CheckedFunction2<String, RequestSender, Response> executor) {
        this.executor = executor;
    }

    public CheckedFunction2<String, RequestSender, Response> getExecutor() {
        return executor;
    }

    private static Response get(String path, RequestSender sender) {
        return sender.get(path);
    }

    private static Response post(String path, RequestSender sender) {
        return sender.post(path);
    }

    private static Response put(String path, RequestSender sender) {
        return sender.put(path);
    }

    private static Response patch(String path, RequestSender sender) {
        return sender.patch(path);
    }

    private static Response delete(String path, RequestSender sender) {
        return sender.delete(path);
    }

    private static Response unSupport(String path, RequestSender sender) {
        throw new ApiTestPlatformException(ErrorCode.NOT_SUPPORT_METHOD);
    }


    private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }


    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        return (method != null ? mappings.get(method) : null);
    }


    public boolean matches(String method) {
        return (this == resolve(method));
    }
}
