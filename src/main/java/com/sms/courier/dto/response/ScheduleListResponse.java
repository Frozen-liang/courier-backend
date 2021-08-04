package com.sms.courier.dto.response;


import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.CycleType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ScheduleListResponse extends LookupUserResponse {

    private String name;

    private String description;

    @JsonProperty("isLoop")
    private boolean loop;

    private CycleType cycle;

    private List<String> time;

    private List<Integer> week;

    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime lastTaskCompleteTime;


}
