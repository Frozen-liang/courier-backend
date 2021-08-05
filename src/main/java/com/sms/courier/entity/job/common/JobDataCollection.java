package com.sms.courier.entity.job.common;

import com.sms.courier.entity.datacollection.TestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class JobDataCollection {

    private String id;

    private String projectId;

    private String collectionName;

    private TestData testData;
}
