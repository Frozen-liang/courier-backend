package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectImportSourceResponse {


    private String id;
    private String name;
    private Integer documentType;
    private Integer saveMode;
    private String projectId;
    private String documentUrl;
    private Integer importStatus;
    /**
     * 新API文件状态预设.
     */
    private Integer apiPresetStatus;
    /**
     * 变更API状态设置.
     */
    private Integer apiChangeStatus;
    private String createUser;
    private String modifyUser;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime modifyDateTime;
}
