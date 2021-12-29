package com.sms.courier.utils;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdUtilTest {

    @Test
    public void generatorId_test(){
        Long aLong = IdUtil.generatorId();
        assertThat(aLong).isNotNull();
    }
}
