package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneCaseCommentRequest {

    @NotBlank(groups = {UpdateGroup.class}, message = "The id must not be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The sceneCaseId must not null.")
    private String sceneCaseId;

    private String parentId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The comment must not null.")
    private String comment;

}
