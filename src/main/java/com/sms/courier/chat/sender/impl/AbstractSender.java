package com.sms.courier.chat.sender.impl;

import com.sms.courier.chat.sender.Sender;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSender implements Sender {

    protected String handleVariable(String template, String variableKey, Object variable) {
        if (StringUtils.isNotBlank(variableKey)) {
            return processTemplate(template, variableKey, variable);
        }
        return template;
    }

    protected abstract String processTemplate(String template, String variableKey, Object variable);

}