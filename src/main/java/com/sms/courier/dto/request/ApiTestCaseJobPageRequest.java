package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ApiTestCaseJobPageRequest extends PageDto {

    private List<String> userIds;

    private String apiTestCaseId;

    private String apiId;

}
