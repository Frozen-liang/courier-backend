package com.sms.satp.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataCollectionImportRequest {

    @NotEmpty(message = "The id must be not empty.")
    private String id;
    @Range(min = 1, max = 2, message = "The importMode must between 1 and 2.")
    private Integer importMode;
    @NotNull(message = "The file must be not null.")
    private MultipartFile file;
}
