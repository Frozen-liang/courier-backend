package com.sms.courier.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.courier.common.constant.TimePatternConstant;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiSettingRequest {


    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The name cannot be empty.")
    private String name;
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The description cannot be empty.")
    private String description;
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The expireTime cannot be empty.")
    @JsonFormat(pattern = TimePatternConstant.DEFAULT_PATTERN)
    private LocalDateTime expireTime;
}
