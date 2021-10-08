package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobRequest extends PageDto {

    private String scheduleRecordId;

    private String caseId;
}
