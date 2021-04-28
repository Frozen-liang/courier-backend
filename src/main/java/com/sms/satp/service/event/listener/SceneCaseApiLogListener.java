package com.sms.satp.service.event.listener;

import com.sms.satp.entity.dto.SceneCaseApiLogDto;
import com.sms.satp.service.SceneCaseApiLogService;
import com.sms.satp.service.event.entity.SceneCaseApiLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SceneCaseApiLogListener {

    private final SceneCaseApiLogService sceneCaseApiLogService;

    public SceneCaseApiLogListener(SceneCaseApiLogService sceneCaseApiLogService) {
        this.sceneCaseApiLogService = sceneCaseApiLogService;
    }

    @EventListener
    public void handleEvent(SceneCaseApiLogEvent event) {
        SceneCaseApiLogDto sceneCaseApiLogDto = event.getSceneCaseApiLogDto();
        //query user name by userId set operationUserName
        sceneCaseApiLogService.add(sceneCaseApiLogDto);
    }

}
