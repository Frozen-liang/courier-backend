package com.sms.courier.service;

import com.sms.courier.dto.request.PasswordResetByEmailRequest;

public interface UserComplexService {

    Boolean sendResetEmail(String email);

    Boolean resetPwdByEmail(PasswordResetByEmailRequest request);
}
