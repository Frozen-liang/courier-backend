package com.sms.satp.entity.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class BaseVerification {

    @Field("isCheckStatus")
    private boolean checkStatus;
}
