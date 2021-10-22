package com.sms.courier.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class BaseEntity {

    @MongoId(FieldType.OBJECT_ID)
    @JsonIgnore
    private String id;
    @Field(name = "isRemoved")
    @JsonIgnore
    private boolean removed;
    @CreatedBy
    @Field(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    private String createUserId;
    @LastModifiedBy
    @Field(targetType = FieldType.OBJECT_ID)
    @JsonIgnore
    private String modifyUserId;
    @CreatedDate
    @JsonIgnore
    private LocalDateTime createDateTime;
    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime modifyDateTime;
}
