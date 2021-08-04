package com.sms.satp.entity.schedule;


import com.sms.satp.common.enums.CaseFilter;
import com.sms.satp.common.enums.CycleType;
import com.sms.satp.common.enums.NoticeType;
import com.sms.satp.common.enums.ScheduleStatusType;
import com.sms.satp.common.enums.TaskStatus;
import com.sms.satp.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    private String name;

    private String description;

    @Field(targetType = FieldType.OBJECT_ID)
    private String envId;

    @Field(name = "isLoop")
    private boolean loop;

    private CycleType cycle;

    private List<String> time;

    private List<Integer> week;

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

}
