package com.sms.satp.common.listener.event;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ApiDeleteEvent extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = 7099057708183579526L;

    private List<String> apiIds;

    public ApiDeleteEvent(Object source, List<String> apiIds) {
        super(source);
        this.apiIds = apiIds;
    }
}
