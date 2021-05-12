package com.sms.satp.entity.project;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.SaveMode;
import com.sms.satp.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document("ProjectImportSource")
public class ProjectImportSourceEntity extends BaseEntity {


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
}
