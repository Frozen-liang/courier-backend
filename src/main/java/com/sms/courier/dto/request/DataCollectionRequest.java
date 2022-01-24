package com.sms.courier.dto.request;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataCollectionRequest {

    @NotBlank(groups = UpdateGroup.class, message = "The id cannot be empty.")
    @Null(groups = InsertGroup.class, message = "The id must be null.")
    private String id;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId cannot be empty")
    private String projectId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The groupId cannot be empty")
    private String groupId;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The collectionName cannot be empty")
    private String collectionName;

    private List<String> paramList;

    @Valid
    private List<TestDataRequest> dataList;

    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "The envId cannot be empty")
    private String envId;
}
