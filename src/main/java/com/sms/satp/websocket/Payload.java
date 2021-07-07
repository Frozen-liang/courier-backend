package com.sms.satp.websocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload<T> {

    private boolean success;
    private String message;
    private T data;

    @SuppressWarnings("rawtypes")
    public static PayloadBuilder ok() {
        return Payload.builder().success(true).message("success");
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Payload<T> ok(T data) {
        return ok().data(data).build();
    }

    @SuppressWarnings("rawtypes")
    public static Payload fail() {
        return Payload.builder().success(false).message("fail").build();
    }

    @SuppressWarnings("rawtypes")
    public static Payload fail(String message) {
        return Payload.builder().success(false).message(message).build();
    }

    public static <T> Payload<T> createPayload(boolean success, String message, T data) {
        return new Payload<>(success, message, data);
    }


}
