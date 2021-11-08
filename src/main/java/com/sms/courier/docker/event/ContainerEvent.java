package com.sms.courier.docker.event;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Event;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.repository.CommonRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContainerEvent extends ResultCallbackTemplate<ContainerEvent, Event> {

    private final CommonRepository commonRepository;
    private static final String CONTAINER_NAME = "containerName";
    private static final String CONTAINER_STATUS = "containerStatus";
    private static final String NAME = "name";
    private static final String TYPE = "type";

    public ContainerEvent(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    public void onNext(Event event) {
        try {
            log.info("Event: {}", event);
            ContainerStatus containerStatus = ContainerStatus.resolverByName(event.getStatus());
            Objects.requireNonNull(containerStatus, "The container status: " + event.getStatus() + "not exits!");
            Optional.ofNullable(event.getActor()).ifPresent(eventActor -> {
                Map<String, String> attributes = Objects.requireNonNullElse(eventActor.getAttributes(),
                    new HashMap<>());
                String containerName = attributes.get(NAME);
                String type = attributes.get(TYPE);
                log.info("ContainerName: {}, type: {}", containerName, type);
                LabelType labelType = LabelType.resolverByName(type);
                Objects.requireNonNull(labelType, "The label type: " + type + "not exits!");
                updateStatus(containerName, labelType, containerStatus);
                // TODO send email.
            });
        } catch (Exception e) {
            log.error("Handle container event error!", e);
        }

    }

    private void updateStatus(String containerName, LabelType labelType, ContainerStatus containerStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where(CONTAINER_NAME).is(containerName));
        Update update = new Update();
        update.set(CONTAINER_STATUS, containerStatus);
        commonRepository.updateField(query, update, labelType.getEntityClass());
    }
}
