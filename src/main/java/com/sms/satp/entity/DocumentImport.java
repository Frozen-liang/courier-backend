package com.sms.satp.entity;

import com.sms.satp.parser.common.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentImport {

    private String url;
    private String content;
    private DocumentType type;
    private String projectId;
}