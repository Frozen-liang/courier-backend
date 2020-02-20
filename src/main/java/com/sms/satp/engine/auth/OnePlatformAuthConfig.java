package com.sms.satp.engine.auth;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "api.auth.one-platform")
public class OnePlatformAuthConfig {

    public static final String IS_SUPPORT_DST = "isSupportDST";
    public static final String UTC_OFFSET = "utcOffset";
    private String formAction = "/Account/Login";
    private String userNameInputField = "userName";
    private String passwordInputField = "password";
    private String userName;
    private String password;
    private String utcOffset = "-480";
    private boolean supportDst = false;
    private boolean loggingEnabled = false;
    private List<String> additionalInputFieldNames = Lists.newArrayList("__RequestVerificationToken",
        IS_SUPPORT_DST, UTC_OFFSET);


    public boolean hasAdditionalInputFieldNames() {
        return CollectionUtils.isNotEmpty(additionalInputFieldNames);
    }
}
