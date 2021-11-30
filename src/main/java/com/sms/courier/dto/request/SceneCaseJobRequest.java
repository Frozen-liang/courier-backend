package com.sms.courier.dto.request;

import com.sms.courier.dto.PageDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SceneCaseJobRequest extends PageDto {

    private List<String> userIds;

    private String sceneCaseId;

    private String caseTemplateId;

    private String envId;
}
