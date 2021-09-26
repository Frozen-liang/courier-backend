package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "The projectId must not bu empty.")
    private String projectId;

    private boolean execute = true;
}
