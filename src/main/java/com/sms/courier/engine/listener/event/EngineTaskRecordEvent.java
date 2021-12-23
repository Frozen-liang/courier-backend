package com.sms.courier.engine.listener.event;

import lombok.Getter;

@Getter
public class EngineTaskRecordEvent {

    private final String jobId;
    private final String name;
    private final String taskType;
    private final String jobType;

    public EngineTaskRecordEvent(String jobId, String name, String taskType, String jobType) {
        this.jobId = jobId;
        this.name = name;
        this.taskType = taskType;
        this.jobType = jobType;
    }
}
