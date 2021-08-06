package com.sms.courier.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Integer apiPresetStatus;

    private Integer apiChangeStatus;
}
