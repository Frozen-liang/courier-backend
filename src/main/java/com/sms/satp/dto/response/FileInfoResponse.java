package com.sms.satp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FileInfoResponse {

    private String id;

    private String filename;

    private String projectId;

    private long length;

    private String uploadDate;

    private String uploadUser;
}
