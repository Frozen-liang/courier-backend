package com.sms.courier.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobRecord {

    // 用例Id
    private String caseId;

    // 用例名称
    private String caseName;

    // 场景数量
    @Default
    private int sceneCount = 1;

    // 成功数量
    private int success;

    // 失败数量
    private int fail;
}
