package com.sms.satp.dto.request;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataCollectionRequest {

    @NotEmpty(groups = UpdateGroup.class, message = "The id cannot be empty.")
    private String id;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The projectId cannot be empty")
    private String projectId;

    @NotEmpty(groups = {InsertGroup.class, UpdateGroup.class}, message = "The collectionName cannot be empty")
    private String collectionName;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The paramList cannot be null")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "The paramList cannot be empty")
    private List<String> paramList;

    @Valid
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class}, message = "The dataList cannot be null")
    @Size(min = 1, groups = {InsertGroup.class, UpdateGroup.class}, message = "The dataList cannot be empty")
    private List<TestDataRequest> dataList;
}
