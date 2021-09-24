package com.sms.courier.common.listener;

import static com.sms.courier.common.enums.TaskStatus.COMPLETE;
import static com.sms.courier.common.field.ScheduleField.TASK_STATUS;
import static com.sms.courier.common.field.ScheduleRecordField.JOB_IDS;
import static com.sms.courier.common.field.ScheduleRecordField.JOB_RECORDS;
import static com.sms.courier.common.field.ScheduleRecordField.VERSION;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.listener.event.ScheduleJobRecordEvent;
import com.sms.courier.entity.schedule.JobRecord;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleJobRecordListener {

    private final ScheduleRecordRepository scheduleRecordRepository;
    private final CommonRepository commonRepository;

    public ScheduleJobRecordListener(ScheduleRecordRepository scheduleRecordRepository,
        CommonRepository commonRepository) {
        this.scheduleRecordRepository = scheduleRecordRepository;
        this.commonRepository = commonRepository;
    }

    @EventListener
    public void doProcess(ScheduleJobRecordEvent event) {
        try {
            String id = event.getId();
            scheduleRecordRepository.findById(id).ifPresent(scheduleRecord -> {
                log.info("Update schedule record. id={}", scheduleRecord.getId());
                while (true) {
                    Boolean res = updateJobIds(event, scheduleRecord);
                    if (res) {
                        break;
                    } else {
                        scheduleRecord = scheduleRecordRepository.findById(id).orElseThrow();
                    }
                }
                if (scheduleRecord.getJobIds().isEmpty()) {
                    log.info("Update schedule task status is complete");
                    commonRepository.updateFieldById(scheduleRecord.getScheduleId(), Map.of(TASK_STATUS, COMPLETE),
                        ScheduleEntity.class);
                }
            });
        } catch (Exception e) {
            log.error("Update schedule job record error.", e);
        }
    }

    private Boolean updateJobIds(ScheduleJobRecordEvent event, ScheduleRecordEntity scheduleRecord) {
        String jobId = event.getJobId();
        List<String> jobIds = scheduleRecord.getJobIds();
        jobIds.remove(jobId);
        List<JobRecord> jobRecords = scheduleRecord.getJobRecords();
        for (JobRecord jobRecord : jobRecords) {
            if (event.getCaseId().equals(jobRecord.getCaseId())) {
                if (event.getJobStatus() == JobStatus.SUCCESS) {
                    // 成功记录加1
                    int success = jobRecord.getSuccess();
                    jobRecord.setSuccess(++success);
                } else {
                    // 失败记录加1
                    int fail = jobRecord.getFail();
                    jobRecord.setFail(++fail);
                }
            }
        }
        Query query = new Query();
        query.addCriteria(Criteria.where(CommonField.ID.getName()).is(scheduleRecord.getId()));
        query.addCriteria(Criteria.where(VERSION.getName()).is(scheduleRecord.getVersion()));
        Update update = new Update();
        // version加1
        update.inc(VERSION.getName());
        update.set(JOB_IDS.getName(), jobIds);
        update.set(JOB_RECORDS.getName(), jobRecords);
        return commonRepository.updateField(query, update, ScheduleRecordEntity.class);
    }
}
