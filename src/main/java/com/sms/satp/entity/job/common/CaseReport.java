package com.sms.satp.entity.job.common;

import com.sms.satp.entity.api.common.ParamInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseReport {

    private String caseId;

    private Boolean result;

    private String failMessage;

    private Object response;

    private List<ParamInfo> responseHeaders;

    private Long runtime;

    private String paramData;
}
