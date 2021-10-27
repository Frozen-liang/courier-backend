package com.sms.courier.dto.response;


import static com.sms.courier.common.constant.TimePatternConstant.DEFAULT_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.TaskStatus;
import com.sms.courier.entity.schedule.CaseCondition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("isLoop")
    private boolean loop;

    @JsonProperty("isOpen")
    @Field("isOpen")
    private boolean open;

    private Integer cycle;

    private List<String> time;

    private List<Integer> week;

    private Integer caseFilter;

    private CaseCondition caseCondition;

    private List<String> caseIds;

    private Integer noticeType;

    private TaskStatus taskStatus;

    @JsonFormat(pattern = DEFAULT_PATTERN)
    private LocalDateTime lastTaskCompleteTime;

    private List<String> emails;

}
