package com.sms.satp.entity.project;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.DocumentUrlType;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.SaveMode;
import com.sms.satp.entity.BaseEntity;
import com.sms.satp.parser.common.DocumentDefinition;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@Builder
public class ImportSourceVo {


    private DocumentType documentType;
    private SaveMode saveMode;
    private String projectId;
    private String source;
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
