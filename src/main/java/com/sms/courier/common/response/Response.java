package com.sms.courier.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    private String message;
    private String code;
    private T data;

    @SuppressWarnings("rawtypes")
    public static ResponseBuilder ok() {
        return Response.builder().code("200").message("success");
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Response<T> ok(T data) {
        return ok().data(data).build();
    }

    @SuppressWarnings("rawtypes")
    public static Response error(String code, String message) {
        return Response.builder().code(code).message(message).build();
    }

}

