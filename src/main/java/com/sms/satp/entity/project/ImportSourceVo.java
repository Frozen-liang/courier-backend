package com.sms.satp.entity.project;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.DocumentType;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.SaveMode;
import lombok.Builder;
import lombok.Data;

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
