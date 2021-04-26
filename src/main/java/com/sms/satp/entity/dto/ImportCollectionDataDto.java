package com.sms.satp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImportCollectionDataDto {

    private String projectId;
    private MultipartFile dataFile;
    private String collectionName;
}
