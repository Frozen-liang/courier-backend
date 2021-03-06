package com.sms.courier.dto.request;

import com.sms.courier.common.constant.TimePatternConstant;
import com.sms.courier.dto.PageDto;
import java.time.LocalDateTime;
import java.util.List;
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

    private String refId;

    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime queryBeginTime;

    @DateTimeFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime queryEndTime;

    private List<Integer> operationType;

    private List<Integer> operationModule;

    private String operationDesc;

    private List<String> operatorId;

    private List<String> sourceId;
}
