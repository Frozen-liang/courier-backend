package com.sms.courier.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberUtilTest {

    @Test
    public void getPercentageTest() {
        double dto = NumberUtil.getPercentage(50,100);
        assertThat(dto).isEqualTo(0.5);
    }

}
