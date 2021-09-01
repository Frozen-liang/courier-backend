package com.sms.courier.inner;

public interface UserRestVerifyCodeService {

    boolean verifyCode(String email, String code);

    String generateCode(String email);

    void verifyIfProcessing(String email);
}