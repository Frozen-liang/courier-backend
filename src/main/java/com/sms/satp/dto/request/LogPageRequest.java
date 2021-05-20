package com.sms.satp.dto.request;

import com.sms.satp.common.constant.TimePatternConstant;
import com.sms.satp.dto.PageDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class LogPageRequest extends PageDto {

    private String projectId;

    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime queryBeginTime;

    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime queryEndTime;

    private Integer operationType;

    private Integer operationModule;

    private String operationDesc;

    private String operator;

    private Long operatorId;
}
