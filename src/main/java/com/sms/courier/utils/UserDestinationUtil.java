package com.sms.courier.utils;

public abstract class UserDestinationUtil {

    private static final String PREFIX = "/user";
    private static final String CASE = PREFIX + "/case/";
    private static final String SCENE_CASE = PREFIX + "/scene/case/";
    private static final String PROJECT = PREFIX + "/project/";

    public static String getCaseDest(String userId) {
        return CASE + userId;
    }

    public static String getSceneCaseDest(String userId) {
        return SCENE_CASE + userId;
    }

    public static String getProjectDest(String projectId) {
        return PROJECT + projectId;
    }

}
