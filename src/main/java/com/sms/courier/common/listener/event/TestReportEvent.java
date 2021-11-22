package com.sms.courier.common.listener.event;

import com.sms.courier.chat.common.NotificationTemplateType;
import com.sms.courier.common.enums.NoticeType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestReportEvent {

    private final NotificationTemplateType type;
    private final List<String> emails;
    private final NoticeType noticeType;
    private final long success;
    private final long fail;
    private final String name;
    private final String dataName;
    private final Integer totalTimeCost;
    private final Integer paramsTotalTimeCost;
    private final Integer delayTimeTotalTimeCost;
    private final String testCompletionTime;
    private final String testStartTime;
    private final String projectId;
}


