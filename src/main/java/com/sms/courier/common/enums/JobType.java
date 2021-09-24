package com.sms.courier.common.enums;


import com.sms.courier.common.constant.Constants;

public enum JobType {
    CASE(Constants.CASE_SERVICE),
    SCHEDULE_CATE(Constants.SCHEDULE_CASE_SERVICE),
    SCENE_CASE(Constants.SCENE_CASE_SERVICE),
    SCHEDULER_SCENE_CASE(Constants.SCHEDULE_SCENE_CASE_SERVICE);

    private final String serviceName;

    JobType(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
