package com.sms.courier.dto.response;

import com.sms.courier.docker.entity.PortMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
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
    @Default
    private Map<String, String> envVariable = new HashMap<>();
    private List<PortMapping> portMappings;
}
