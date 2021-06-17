package com.sms.satp.common.mongo.listener;

import com.sms.satp.entity.BaseEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BeforeConvertListener {


    private final MongoTemplate mongoTemplate;

    public BeforeConvertListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void listener(@NonNull BeforeConvertEvent<BaseEntity> beforeSaveEvent) {
        if (beforeSaveEvent.getSource() instanceof BaseEntity) {
            BaseEntity baseEntity = beforeSaveEvent.getSource();
            String collectionName = Objects.requireNonNull(beforeSaveEvent.getCollectionName());
            Optional.ofNullable(baseEntity.getId()).ifPresent(id -> {
                BaseEntity oldBaseEntity = mongoTemplate
                    .findById(id, BaseEntity.class, collectionName);
                Optional.ofNullable(oldBaseEntity).ifPresent(entity -> {
                    baseEntity.setCreateUserId(entity.getCreateUserId());
                    baseEntity.setCreateDateTime(entity.getCreateDateTime());
                });
            });
        }
    }
}
