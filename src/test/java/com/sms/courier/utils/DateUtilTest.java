package com.sms.courier.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilTest {

    @Test
    public void toString_null_test() {
        String s = DateUtil.toString(null);
        assertThat(s).isEqualTo("");

    }

    @Test
    public void toString_notNull_test() {
        String s = DateUtil.toString(LocalDateTime.now());
        assertThat(s).isNotNull();
    }

}
