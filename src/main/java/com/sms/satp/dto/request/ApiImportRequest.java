package com.sms.satp.dto.request;

import com.sms.satp.common.enums.ApiStatus;
import com.sms.satp.common.enums.GroupImportType;
import com.sms.satp.common.enums.SaveMode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiImportRequest {

    @NotNull(message = "The documentFileType must not be null.")
    @Range(min = 0, max = 2)
    private Integer documentFileType;
    @NotBlank(message = "The projectId must not be null.")
    private String projectId;
    @NotNull(message = "The file must not be null.")
    private MultipartFile file;
    @NotNull(message = "The saveMode must not be null.")
    @Range(min = 1, max = 3, message = "The saveMode must be in (1|2|3).")
    private Integer saveMode;
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

    @Default
    private Integer groupImportType = GroupImportType.EVERY_ONCE.getCode();
}
