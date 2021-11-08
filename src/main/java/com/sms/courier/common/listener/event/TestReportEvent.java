package com.sms.courier.common.listener.event;

import com.sms.courier.common.listener.handler.enums.TestReportType;
import com.sms.courier.entity.job.common.JobReport;
import lombok.Getter;

@Getter
public class TestReportEvent {

    private final String id;
    private final JobReport jobReport;
    private final int count;
    private final String name;
    private final String dataName;
    private final TestReportType type;

    private TestReportEvent(String id, JobReport jobReport, int count, String name, String dataName,
        TestReportType type) {
        this.id = id;
        this.jobReport = jobReport;
        this.count = count;
        this.name = name;
        this.dataName = dataName;
        this.type = type;
    }

    public static TestReportEvent createScheduleEvent(String id, JobReport jobReport, int count, String name,
        String dataName) {
        return new TestReportEvent(id, jobReport, count, name, dataName, TestReportType.SCHEDULER);
    }
}


