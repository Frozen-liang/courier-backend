package com.sms.satp.pojo.scenes;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Cookie implements NameAndValue {
    private String name;
    private String value;
    private boolean ref;
}
