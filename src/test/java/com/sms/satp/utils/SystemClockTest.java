package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for SystemClock")
public class SystemClockTest {

    @Test
    @DisplayName("Test the now method in the SystemClock")
    public void now_test() {
        assertThat(SystemClock.now()).isNotNull();
    }

    @Test
    @DisplayName("Test the nowDate method in the SystemClock")
    public void nowDate_test() {
        assertThat(SystemClock.nowDate()).isNotNull();
    }
}
