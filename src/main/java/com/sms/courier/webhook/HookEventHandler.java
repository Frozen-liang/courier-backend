package com.sms.courier.webhook;

import com.sms.courier.repository.WebhookRepository;
import com.sms.courier.utils.MustacheUtils;
import com.sms.courier.webhook.model.WebhookEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class HookEventHandler implements InitializingBean, DisposableBean {

    private final PriorityBlockingQueue<WebhookEvent<?>> webhookEventQueue = new PriorityBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1,
        new CustomizableThreadFactory("web-hook-thread"));
    private final RestTemplate restTemplate;
    private final WebhookRepository webhookRepository;

    public HookEventHandler(RestTemplate restTemplate, WebhookRepository webhookRepository) {
        this.restTemplate = restTemplate;
        this.webhookRepository = webhookRepository;
    }

    @EventListener
    @Async("commonExecutor")
    public void handle(WebhookEvent<?> webhookEvent) {
        webhookEventQueue.add(webhookEvent);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("WebHOOK event post initialization >>>>>>>>");
        executorService.execute(this::post);
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }


    public void post() {

        while (true) {
            try {
                WebhookEvent<?> webhookEvent = webhookEventQueue.take();
                List<WebhookEntity> webhookEntities = webhookRepository
                    .findByWebhookType(webhookEvent.getWebhookType());
                for (WebhookEntity webhookEntity : webhookEntities) {
                    try {
                        HttpHeaders header = new HttpHeaders();
                        webhookEntity.getHeader().forEach(header::add);
                        header.setContentType(MediaType.APPLICATION_JSON);
                        String body = MustacheUtils.getContent(webhookEntity.getPayload(), webhookEvent.getData());
                        HttpEntity<String> httpEntity = new HttpEntity<>(body, header);
                        restTemplate.exchange(webhookEntity.getUrl(), HttpMethod.POST, httpEntity, String.class);
                    } catch (Exception e) {
                        log.error("Webhook network exception! url: {}", webhookEntity.getUrl(), e);
                    }
                }
            } catch (InterruptedException e) {
                log.error("Webhook interrupted Exception", e);
            } catch (Exception e) {
                log.error("Webhook Exception", e);
            }
        }
    }

}
