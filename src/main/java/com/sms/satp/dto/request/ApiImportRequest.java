package com.sms.satp.dto.request;

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
}
