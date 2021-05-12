package com.sms.satp.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@SuperBuilder
@NoArgsConstructor
@Data
public class BaseEntity {

    @MongoId(FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String id;
    private Boolean removed;
    @CreatedBy
    private Long createUserId;
    @LastModifiedBy
    private Long modifyUserId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
