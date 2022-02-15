package com.sms.courier.common.listener;

import static com.sms.courier.common.enums.TaskStatus.COMPLETE;
import static com.sms.courier.common.field.ScheduleField.LAST_TASK_COMPLETE_TIME;
import static com.sms.courier.common.field.ScheduleField.TASK_STATUS;
import static com.sms.courier.common.field.ScheduleRecordField.FAIL;
import static com.sms.courier.common.field.ScheduleRecordField.JOB_IDS;
import static com.sms.courier.common.field.ScheduleRecordField.SUCCESS;
import static com.sms.courier.common.field.ScheduleRecordField.TEST_COMPLETION_TIME;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.listener.event.ScheduleJobRecordEvent;
import com.sms.courier.common.listener.event.TestReportEvent;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.utils.DateUtil;
import com.sms.courier.webhook.WebhookEvent;
import com.sms.courier.webhook.enums.WebhookType;
import com.sms.courier.webhook.response.WebhookScheduleResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleJobRecordListener {

    private final MongoTemplate mongoTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final String CASE_ID = "jobRecords.caseId";
    private static final String JOB_RECORD_SUCCESS = "jobRecords.$.success";
    private static final String JOB_RECORD_FAIL = "jobRecords.$.fail";

    public ScheduleJobRecordListener(MongoTemplate mongoTemplate,
        ApplicationEventPublisher applicationEventPublisher) {

        this.mongoTemplate = mongoTemplate;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @EventListener
    public void doProcess(ScheduleJobRecordEvent event) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(CommonField.ID.getName()).is(event.getId()));
            query.addCriteria(Criteria.where(CASE_ID).is(event.getCaseId()));
            Update update = new Update();
            update.pull(JOB_IDS.getName(), event.getJobId());
            update.set(TEST_COMPLETION_TIME.getName(), LocalDateTime.now());
            if (event.getJobStatus() == JobStatus.SUCCESS) {
                update.inc(JOB_RECORD_SUCCESS);
                update.inc(SUCCESS.getName());
            } else {
                update.inc(JOB_RECORD_FAIL);
                update.inc(FAIL.getName());
            }
            ScheduleRecordEntity scheduleRecord = mongoTemplate
                .findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ScheduleRecordEntity.class);
            updateScheduleAndSendEmail(scheduleRecord);
        } catch (Exception e) {
            log.error("Update schedule job record error.", e);
        }
    }

    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    private void updateScheduleAndSendEmail(ScheduleRecordEntity scheduleRecord) {
        if (Objects.nonNull(scheduleRecord) && scheduleRecord.getJobIds().isEmpty()) {
            log.info("Update schedule task status is complete");
            Query query =
                Query.query(Criteria.where(CommonField.ID.getName()).is(scheduleRecord.getScheduleId()));
            Update update = new Update();
            update.set(TASK_STATUS.getName(), COMPLETE);
            update.set(LAST_TASK_COMPLETE_TIME.getName(), LocalDateTime.now());
            ScheduleEntity scheduleEntity = mongoTemplate.findAndModify(query, update, ScheduleEntity.class);
            TestReportEvent testReportEvent = TestReportEvent.builder()
                .type(NotificationTemplateType.SCHEDULE_TEST_REPORT)
                .noticeType(scheduleEntity.getNoticeType())
                .emails(scheduleEntity.getEmails())
                .name(scheduleEntity.getName())
                .fail(scheduleRecord.getFail())
                .success(scheduleRecord.getSuccess())
                .testStartTime(DateUtil.toString(scheduleRecord.getCreateDateTime()))
                .testCompletionTime(DateUtil.toString(scheduleRecord.getTestCompletionTime()))
                .projectId(scheduleEntity.getProjectId())
                .build();
            applicationEventPublisher.publishEvent(testReportEvent);
            publisherWebhook(scheduleRecord);
        }
    }

    private void publisherWebhook(ScheduleRecordEntity scheduleRecord) {
        WebhookScheduleResponse webhookScheduleResponse = WebhookScheduleResponse.builder()
            .id(scheduleRecord.getScheduleId())
            .name(scheduleRecord.getScheduleName()).success(scheduleRecord.getSuccess())
            .metadata(scheduleRecord.getMetadata())
            .fail(scheduleRecord.getFail()).build();
        applicationEventPublisher
            .publishEvent(WebhookEvent.create(WebhookType.SCHEDULE, webhookScheduleResponse));
    }
}
