package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class BaseResponse {

    private String id;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
    private Boolean removed;
}
