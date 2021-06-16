package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
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

    @JsonFormat(pattern = DEFAULT_PATTERN, timezone = "GMT+8")
    private Date uploadDate;

    private String uploadUser;
}
