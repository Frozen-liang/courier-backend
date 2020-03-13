package com.sms.satp.entity.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentImportDto {

    private String url;
    private MultipartFile file;
    @NotNull(message = "DocumentType cannot be empty")
    private String type;
    @NotNull(message = "ProjectId cannot be empty")
    private String projectId;

}