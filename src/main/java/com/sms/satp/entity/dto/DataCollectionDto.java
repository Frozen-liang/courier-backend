package com.sms.satp.entity.dto;

import com.sms.satp.entity.datacollection.TestData;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataCollectionDto {

    private String id;
    @NotEmpty(message = "The projectId cannot be empty")
    private String projectId;
    @NotEmpty(message = "The collectionName cannot be empty")
    private String collectionName;
    @NotEmpty.List(@NotEmpty(message = "The paramList cannot be empty"))
    private List<String> paramList;
    private List<TestData> dataList;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
