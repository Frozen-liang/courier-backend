package com.sms.satp.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTemplateDetailResponse {

    private CaseTemplateResponse caseTemplateResponse;

    private List<CaseTemplateApiResponse> caseTemplateApiResponseList;
}
