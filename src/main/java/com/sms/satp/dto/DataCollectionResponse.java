package com.sms.satp.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataCollectionResponse {

    private String id;
    private String projectId;
    private String collectionName;
    private List<String> paramList;
    private List<TestDataResponse> dataList;
    private String createDateTime;
    private String modifyDateTime;
    private Long createUserId;
    private Long modifyUserId;
}
