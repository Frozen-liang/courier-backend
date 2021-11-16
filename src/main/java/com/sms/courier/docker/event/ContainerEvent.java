package com.sms.courier.docker.event;

import static com.sms.courier.docker.enmu.ContainerField.CONTAINER_NAME;
import static com.sms.courier.docker.enmu.ContainerField.CONTAINER_STATUS;
import static com.sms.courier.docker.enmu.ContainerField.NAME;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Event;
import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.docker.enmu.LabelType;
import com.sms.courier.repository.CommonRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    private static final String TYPE = "type";

    public ContainerEvent(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void onNext(Event event) {
        try {
            log.info("Event: {}", event);
            ContainerStatus containerStatus = ContainerStatus.resolverByName(event.getStatus());
            Objects.requireNonNull(containerStatus, "The container status: " + event.getStatus() + "not exits!");
            Optional.ofNullable(event.getActor()).ifPresent(eventActor -> {
                Map<String, String> attributes = Objects.requireNonNullElse(eventActor.getAttributes(),
                    new HashMap<>());
                String containerName = attributes.get(NAME.getName());
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
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where(CONTAINER_NAME.getName()).is(containerName),
            Criteria.where(NAME.getName()).is(containerName));
        query.addCriteria(criteria);
        Update update = new Update();
        update.set(CONTAINER_STATUS.getName(), containerStatus);
        commonRepository.updateField(query, update, labelType.getEntityClass());
    }
}
