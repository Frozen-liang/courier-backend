package com.sms.courier.entity.schedule;


import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.CycleType;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.common.enums.ScheduleStatusType;
import com.sms.courier.common.enums.TaskStatus;
import com.sms.courier.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Document(collection = "Schedule")
public class ScheduleEntity extends BaseEntity {

    @Field(targetType = FieldType.OBJECT_ID)
    private String groupId;

    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    private String name;

    private String description;

    @Field(targetType = FieldType.OBJECT_ID)
    private String envId;

    @Field(name = "isLoop")
    private boolean loop;

    private CycleType cycle;

    private Set<String> time;

    private Set<Integer> week;

    private CaseFilter caseFilter;

    private CaseCondition caseCondition;

    private NoticeType noticeType;

    private List<String> userIds;

    private ScheduleStatusType scheduleStatus;

    // Status of the previous task.
    private TaskStatus taskStatus;

    private LocalDateTime lastTaskCompleteTime;

    @Field("isDisplayError")
    private boolean displayError;

    @Field("isOpen")
    @Default
    private boolean open = true;

}
