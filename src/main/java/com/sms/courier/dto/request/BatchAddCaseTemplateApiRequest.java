package com.sms.courier.dto.request;

import java.util.List;
import javax.validation.Valid;
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

    @Valid
    @NotEmpty(message = "The entity can not be empty")
    private List<AddCaseTemplateApiRequest> addCaseTemplateApiRequestList;
}
