package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ScheduleRecordPageRequest extends PageDto {

    private String scheduleName;

    private String projectId;

    private String scheduleId;

    private boolean execute = true;
}
