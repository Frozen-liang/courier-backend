package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.ACCOUNT_NOT_EXIST;
import static com.sms.courier.common.exception.ErrorCode.USER_RESET_CODE_WRONG;

import com.sms.courier.chat.common.AdditionalParam;
import com.sms.courier.chat.modal.NotificationPayload;
import com.sms.courier.chat.modal.ResetEmailModel;
import com.sms.courier.chat.sender.Sender;
import com.sms.courier.dto.request.PasswordResetByEmailRequest;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.inner.UserRestVerifyCodeService;
import com.sms.courier.service.UserComplexService;
import com.sms.courier.service.UserService;
import com.sms.courier.utils.Assert;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserComplexServiceImpl implements UserComplexService {

    private final UserService userService;
    private final Sender sender;
    private final UserRestVerifyCodeService userRestVerifyCodeService;

    public UserComplexServiceImpl(UserService userService,
        Sender sender, UserRestVerifyCodeService userRestVerifyCodeService) {
        this.userService = userService;
        this.sender = sender;
        this.userRestVerifyCodeService = userRestVerifyCodeService;
    }

    @Override
    public Boolean sendResetEmail(String email) {
        userRestVerifyCodeService.verifyIfProcessing(email);
        Optional<UserEntity> optional = userService.findByEmail(email);
        Assert.isTrue(optional.isPresent(), ACCOUNT_NOT_EXIST);
        UserEntity userEntity = optional.get();
        String code = userRestVerifyCodeService.generateCode(email);
        Map<AdditionalParam, Object> param = new HashMap<>();
        param.put(AdditionalParam.EMAIL_TO, Collections.singletonList(email));
        ResetEmailModel resetEmailModel = ResetEmailModel.builder().name(userEntity.getUsername()).code(code).build();
        NotificationPayload notificationPayload = NotificationPayload.builder()
            .titleVariable(resetEmailModel)
            .contentVariable(resetEmailModel)
            .additionalParam(param)
            .build();
        return sender.sendResetPwdNotification(notificationPayload);
    }

    @Override
    public Boolean resetPwdByEmail(PasswordResetByEmailRequest request) {
        boolean verify = userRestVerifyCodeService.verifyCode(request.getEmail(), request.getCode());
        Assert.isTrue(verify, USER_RESET_CODE_WRONG);
        return userService.setPasswordByEmail(request.getEmail(), request.getPassword());
    }
}