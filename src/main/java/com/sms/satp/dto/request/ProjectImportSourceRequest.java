package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectImportSourceRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id must not be null.")
    private String id;
    @NotBlank(message = "The name must not be null.")
    private String name;
    @NotNull(message = "The documentType must not be null.")
    @Range(min = 0, max = 1, message = "The documentType only either 0 or 1.")
    private Integer documentType;
    @NotNull(message = "The saveMode must not be null.")
    @Range(min = 1, max = 3, message = "The saveMode must be in (1|2|3).")
    private Integer saveMode;
    @NotNull(message = "The projectId must not be null.")
    private String projectId;
    @NotNull(message = "The documentUrl must not be null.")
    private String documentUrl;
    /**
     * 新API文件状态预设.
     */
    @Default
    private Integer apiPresetStatus = ApiStatus.PUBLISH.getCode();
    /**
     * 变更API状态设置.
     */
    @Default
    private Integer apiChangeStatus = ApiStatus.PUBLISH.getCode();
}
