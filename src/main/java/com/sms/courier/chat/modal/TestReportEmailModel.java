package com.sms.courier.chat.modal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestReportEmailModel {


    private String projectId;

    private Long success;

    private Long fail;

    private String name;

    // 定时任务参数
    private String testCompletionTime;

    private String testStartTime;

    // 单个用例参数

    private String dataName;

    private Integer totalTimeCost;

    private Integer paramsTotalTimeCost;

    private Integer delayTimeTotalTimeCost;


}
