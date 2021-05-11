package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectImportSourceResponse {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;
    private Integer documentType;
    private Integer saveMode;
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
    private String documentUrl;
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
