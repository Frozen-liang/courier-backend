package com.sms.courier.security;

import lombok.Getter;

@Getter
public enum TokenType {
    USER,
    ENGINE,
    MOCK,
    OPEN_API
}
