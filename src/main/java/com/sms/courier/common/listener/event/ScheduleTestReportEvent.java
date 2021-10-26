package com.sms.courier.common.listener.event;

import com.sms.courier.entity.job.common.JobReport;
import lombok.Getter;

@Getter
public class ScheduleTestReportEvent {

    private final String id;
    private final JobReport jobReport;
    private final int count;

    private ScheduleTestReportEvent(String id, JobReport jobReport, int count) {
        this.id = id;
        this.jobReport = jobReport;
        this.count = count;
    }

    public static ScheduleTestReportEvent create(String id, JobReport jobReport, int count) {
        return new ScheduleTestReportEvent(id, jobReport, count);
    }
}


