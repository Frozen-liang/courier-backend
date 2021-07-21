package com.sms.satp.security.strategy;

import com.sms.satp.security.TokenType;

public interface SecurityStrategyFactory {

    SatpSecurityStrategy fetchSecurityStrategy(TokenType tokenType);
}