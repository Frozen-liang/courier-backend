package com.sms.courier.dto.response;

import java.util.Map;
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
public class EngineSettingResponse extends BaseResponse {

    private String imageName;
    private String containerName;
    private String version;
    private Integer taskSizeLimit;
    private Map<String, String> envVariable;
}
