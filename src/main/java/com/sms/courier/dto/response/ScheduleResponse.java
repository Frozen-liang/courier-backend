package com.sms.courier.dto.response;


import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.entity.schedule.CaseCondition;
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
public class ScheduleResponse extends BaseResponse {

    private String groupId;

    private String name;

    private String description;

    private String envId;

    @JsonProperty("isLoop")
    private boolean loop;

    private Integer cycle;

    private List<String> time;

    private List<Integer> week;

    private Integer caseFilter;

    private CaseCondition caseCondition;

    private List<String> caseIds;

    private Integer noticeType;

    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime lastTaskCompleteTime;

    private List<String> userIds;

    @JsonProperty("isDisplayError")
    private boolean displayError;

}
