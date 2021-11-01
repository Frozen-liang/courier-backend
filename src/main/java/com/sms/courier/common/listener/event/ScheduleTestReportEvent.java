package com.sms.courier.common.listener.event;

import com.sms.courier.entity.job.common.JobReport;
import lombok.Getter;

@Getter
public class ScheduleTestReportEvent {

    private final String id;
    private final JobReport jobReport;
    private final int count;
    private final String name;
    private final String dataName;

    private ScheduleTestReportEvent(String id, JobReport jobReport, int count, String name, String dataName) {
        this.id = id;
        this.jobReport = jobReport;
        this.count = count;
        this.name = name;
        this.dataName = dataName;
    }

    public static ScheduleTestReportEvent create(String id, JobReport jobReport, int count, String name,
        String dataName) {
        return new ScheduleTestReportEvent(id, jobReport, count, name, dataName);
    }
}


