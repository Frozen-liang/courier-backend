package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
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

    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN, timezone = "GMT+8")
    private Date uploadDate;

    private String uploadUser;
}
