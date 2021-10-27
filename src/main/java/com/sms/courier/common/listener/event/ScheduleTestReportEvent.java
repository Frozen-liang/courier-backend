package com.sms.courier.common.listener.event;

import com.sms.courier.entity.job.common.JobReport;
import lombok.Getter;

@Getter
public class ScheduleTestReportEvent {

    private final String id;
    private final JobReport jobReport;
    private final int count;
    private final String name;

    private ScheduleTestReportEvent(String id, JobReport jobReport, int count, String name) {
        this.id = id;
        this.jobReport = jobReport;
        this.count = count;
        this.name = name;
    }

    public static ScheduleTestReportEvent create(String id, JobReport jobReport, int count, String name) {
        return new ScheduleTestReportEvent(id, jobReport, count, name);
    }
}


