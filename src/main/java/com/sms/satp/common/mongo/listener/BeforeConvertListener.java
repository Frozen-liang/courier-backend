package com.sms.satp.common.mongo.listener;

import com.sms.satp.entity.BaseEntity;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class BeforeConvertListener {


    private final MongoTemplate mongoTemplate;

    public BeforeConvertListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener
    public void listener(BeforeConvertEvent<BaseEntity> beforeSaveEvent) {
        BaseEntity baseEntity = beforeSaveEvent.getSource();
        Optional.ofNullable(baseEntity.getId()).ifPresent(id -> {
            BaseEntity oldBaseEntity = mongoTemplate
                .findById(id, BaseEntity.class, Objects.requireNonNull(beforeSaveEvent.getCollectionName()));
            Optional.ofNullable(oldBaseEntity).ifPresent(entity -> {
                baseEntity.setCreateUserId(entity.getCreateUserId());
                baseEntity.setCreateDateTime(entity.getCreateDateTime());
            });
        });
    }
}
