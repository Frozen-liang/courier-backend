package com.sms.satp.service.event.entity;

import com.sms.satp.dto.SceneCaseApiLogDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SceneCaseApiLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private SceneCaseApiLogDto sceneCaseApiLogDto;

    public SceneCaseApiLogEvent(Object source,SceneCaseApiLogDto sceneCaseApiLogDto) {
        super(source);
        this.sceneCaseApiLogDto = sceneCaseApiLogDto;
    }
}
