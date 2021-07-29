package com.sms.satp.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserDestinationUtil")
public class UserDestinationUtilTest {

    private static final String ID = "test";

    @Test
    @DisplayName("Test the getCaseDest method in the UserDestinationUtil")
    public void getCaseDest_test() {
        assertThat(UserDestinationUtil.getCaseDest(ID)).isEqualTo("/user/case/test");
    }

    @Test
    @DisplayName("Test the getSceneCaseDest method in the UserDestinationUtil")
    public void getSceneCaseDest_test() {
        assertThat(UserDestinationUtil.getSceneCaseDest(ID)).isEqualTo("/user/scene/case/test");
    }

    @Test
    @DisplayName("Test the getProjectDest method in the UserDestinationUtil")
    public void getProjectDest_test() {
        assertThat(UserDestinationUtil.getProjectDest(ID)).isEqualTo("/user/project/test");
    }
}
