package com.sms.satp.dto.response;

import static com.sms.satp.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

    private String id;
    private String name;
    private String description;
    private Boolean enable;
    @JsonProperty("isExist")
    private boolean exist;
    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
}