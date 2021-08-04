package com.sms.courier.security.strategy;

import com.sms.courier.security.TokenType;

public interface SecurityStrategyFactory {

    SatpSecurityStrategy fetchSecurityStrategy(TokenType tokenType);
}