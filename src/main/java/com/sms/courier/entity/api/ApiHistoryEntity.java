package com.sms.courier.entity.api;

import com.sms.courier.entity.api.common.ApiHistoryDetail;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ApiHistory")
public class ApiHistoryEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /**
     * 历史记录.
     */
    private ApiHistoryDetail record;

    /**
     * 修改描述.
     */
    private String description;

    @CreatedDate
    private LocalDateTime createDateTime;

    @CreatedBy
    @Field(targetType = FieldType.OBJECT_ID)
    private String createUserId;
}
