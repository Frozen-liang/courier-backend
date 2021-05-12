package com.sms.satp.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchAddCaseTemplateApiRequest {

    private List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList;
}
