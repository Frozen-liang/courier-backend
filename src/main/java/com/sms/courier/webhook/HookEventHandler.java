package com.sms.courier.webhook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HookEventHandler {

    private final PriorityBlockingQueue<WebhookEvent<?>> webhookEventQueue = new PriorityBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1,
        new CustomizableThreadFactory("web-hook-thread"));

//    @PostConstruct
    public void init() {
        log.info("WebHOOK event post initialization >>>>>>>>");
        executorService.execute(this::post);
    }

    @EventListener
    public void handle(WebhookEvent<?> webhookEvent) {
        webhookEventQueue.add(webhookEvent);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }


    public void post() {

        while (true) {
            try {
                WebhookEvent<?> webhookEvent = webhookEventQueue.take();
                // TODO post event
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
