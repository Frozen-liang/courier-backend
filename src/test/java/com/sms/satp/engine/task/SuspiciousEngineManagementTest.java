package com.sms.satp.engine.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test for SuspiciousEngineManagement")
public class SuspiciousEngineManagementTest {

    private final SuspiciousEngineManagement suspiciousEngineManagement = new SuspiciousEngineManagementImpl();
    private static final String ENGINE_ID = "engine/123/invoke";

    @Test
    @DisplayName("Test for increaseIndex in SuspiciousEngineManagement")
    public void increaseIndex_test() {
        Integer integer = suspiciousEngineManagement.increaseIndex();
        assertThat(integer).isEqualTo(1);
    }

    @Test
    @DisplayName("Test for getCurrentIndex in SuspiciousEngineManagement")
    public void getCurrentIndex_test() {
        Integer integer = suspiciousEngineManagement.getCurrentIndex();
        assertThat(integer).isEqualTo(0);
    }

    @Test
    @DisplayName("Test for add in SuspiciousEngineManagement")
    public void add_test() {
        suspiciousEngineManagement.add(ENGINE_ID);
        for (int i = 0; i < 59; i++) {
            suspiciousEngineManagement.increaseIndex();
        }
        Integer currentIndex = suspiciousEngineManagement.getCurrentIndex();
        List<String> engineIds = suspiciousEngineManagement.get(currentIndex - 1);
        assertThat(engineIds).hasSize(1);
        assertThat(engineIds.get(0)).isEqualTo(ENGINE_ID);
    }

    @Test
    @DisplayName("Test for remove in SuspiciousEngineManagement")
    public void remove_test() {
        suspiciousEngineManagement.add(ENGINE_ID);
        suspiciousEngineManagement.remove(ENGINE_ID);
        List<String> engineIds = suspiciousEngineManagement.get(suspiciousEngineManagement.getCurrentIndex());
        assertThat(engineIds).isNullOrEmpty();
    }

    @Test
    @DisplayName("Test for get in SuspiciousEngineManagement")
    public void remove_get() {
        List<String> engineIds = suspiciousEngineManagement.get(suspiciousEngineManagement.getCurrentIndex());
        assertThat(engineIds).isNullOrEmpty();
    }
}
