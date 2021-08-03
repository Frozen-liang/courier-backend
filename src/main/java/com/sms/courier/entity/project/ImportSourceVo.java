package com.sms.courier.entity.project;

import com.sms.courier.common.enums.ApiStatus;
import com.sms.courier.common.enums.DocumentType;
import com.sms.courier.common.enums.GroupImportType;
import com.sms.courier.common.enums.SaveMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImportSourceVo {

    private String id;
    private String name;
    private DocumentType documentType;
    private SaveMode saveMode;
    private String projectId;
    private String workspaceId;
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
