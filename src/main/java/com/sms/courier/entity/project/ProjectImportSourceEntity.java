package com.sms.courier.entity.project;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.DocumentUrlType;
import com.sms.courier.common.enums.GroupImportType;
import com.sms.courier.common.enums.SaveMode;
import com.sms.courier.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("ProjectImportSource")
@SuperBuilder
public class ProjectImportSourceEntity extends BaseEntity {

    private String name;
    private DocumentUrlType documentType;
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

    private GroupImportType groupImportType;
}
