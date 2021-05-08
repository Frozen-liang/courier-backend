package com.sms.satp.entity.api;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ApiHistory")
public class ApiHistoryEntity {

    @MongoId(FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String id;

    /**
     * 历史记录.
     */
    private ApiEntity record;

    /**
     * 修改描述.
     */
    private String description;

    @CreatedDate
    private LocalDateTime createTime;
}
