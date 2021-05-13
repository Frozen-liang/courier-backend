package com.sms.satp.dto.request;

import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatchAddCaseTemplateApiRequest {

    @NotEmpty(message = "The entity can not be empty")
    private List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList;
}
