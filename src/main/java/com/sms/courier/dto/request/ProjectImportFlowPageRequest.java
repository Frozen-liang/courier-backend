package com.sms.courier.dto.request;

import com.sms.courier.common.constant.TimePatternConstant;
import com.sms.courier.dto.PageDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProjectImportFlowPageRequest extends PageDto {

    private String projectId;
    private String importSourceId;
    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime endTime;
    private Integer importStatus;
}
