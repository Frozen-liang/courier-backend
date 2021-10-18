package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemVersionResponse {

    private String id;
    private String version;
    private String name;
    private String group;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime buildTime;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime modifyDateTime;
}
