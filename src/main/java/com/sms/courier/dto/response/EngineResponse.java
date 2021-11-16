package com.sms.courier.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.constant.TimePatternConstant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngineResponse {

    private String id;
    private String destination;
    private String host;
    private String name;
    /**
     * The engine version.
     */
    private String version;
    private Integer taskSizeLimit;
    private Integer caseTask;
    private Integer sceneCaseTask;
    private Integer taskCount;
    private Integer status;
    private Integer containerStatus;
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime createDateTime;
    @JsonProperty("isOpen")
    private boolean open;
}
