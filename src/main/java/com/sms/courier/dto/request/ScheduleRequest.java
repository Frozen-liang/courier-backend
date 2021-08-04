package com.sms.satp.dto.request;


import com.sms.satp.common.enums.CycleType;
import com.sms.satp.common.enums.NoticeType;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {

    @NotBlank(message = "The id must not be empty.", groups = UpdateGroup.class)
    @Null(message = "The id must be null.", groups = InsertGroup.class)
    private String id;

    private String groupId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name must not be empty.")
    private String projectId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name must not be empty.")
    private String name;

    private String description;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The envId must not be empty.")
    private String envId;

    private boolean loop;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The cycle must not be null.")
    private CycleType cycle;

    @NotNull
    @Size(min = 1)
    private List<String> time;

    private List<Integer> week;

    private NoticeType noticeType;

    private List<String> userIds;

}
