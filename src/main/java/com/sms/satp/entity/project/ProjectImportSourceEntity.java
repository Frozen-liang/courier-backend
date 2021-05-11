package com.sms.satp.entity.project;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.SaveMode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("ProjectImportSource")
public class ProjectImportSourceEntity {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;
    private DocumentType documentType;
    private SaveMode saveMode;
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private String documentUrl;
    /**
     * 新API文件状态预设.
     */
    private ApiStatus apiPresetStatus;
    /**
     * 变更API状态设置.
     */
    private ApiStatus apiChangeStatus;
    @CreatedBy
    private Long createUserId;
    @LastModifiedBy
    private Long modifyUserId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime modifyDateTime;
}
