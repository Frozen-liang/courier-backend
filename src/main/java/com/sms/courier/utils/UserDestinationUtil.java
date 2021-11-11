package com.sms.courier.utils;

public abstract class UserDestinationUtil {

    private static final String PREFIX = "/user";
    private static final String CASE = PREFIX + "/case/";
    private static final String SCENE_CASE = PREFIX + "/scene/case/";
    private static final String PROJECT = PREFIX + "/project/";
    private static final String LOG = PREFIX + "/log/";
    private static final String DOCKER = PREFIX + "/%s/message/%s";

    public static String getCaseDest(String userId) {
        return CASE + userId;
    }

    public static String getSceneCaseDest(String userId) {
        return SCENE_CASE + userId;
    }

    public static String getProjectDest(String projectId) {
        return PROJECT + projectId;
    }

    public static String getLogDest(String id) {
        return LOG + SecurityUtil.getCurrUserId() + "/" + id;
    }

    public static String getDockerDest(String description) {
        return String.format(DOCKER, description, SecurityUtil.getCurrUserId());
    }
}
