package com.sms.courier.chat.modal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetEmailModel {

    private String name;
    private String code;
}
