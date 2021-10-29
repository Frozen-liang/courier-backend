package com.sms.courier.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.enums.CycleType;
import com.sms.courier.common.enums.NoticeType;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.entity.schedule.CaseCondition;
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

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId must not be empty.")
    private String projectId;

    private String workspaceId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name must not be empty.")
    private String name;

    private String description;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The envId must not be empty.")
    private String envId;

    @JsonProperty("isLoop")
    private boolean loop;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The cycle must not be null.")
    private CycleType cycle;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The caseType must not be null.")
    private CaseType caseType;

    private CaseFilter caseFilter;

    private CaseCondition caseCondition;

    private List<String> caseIds;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The time must not be null.")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "The time must not be empty.")
    private List<String> time;

    private List<Integer> week;

    private NoticeType noticeType;

    private List<String> emails;

}
