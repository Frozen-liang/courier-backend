package com.sms.courier.dto.response;

import com.sms.courier.docker.entity.PortMapping;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class MockSettingResponse extends BaseResponse {

    private String mockUrl;
    private String imageName;
    private String containerName;
    private String version;
    private Map<String, String> envVariable;
    private List<PortMapping> portMappings;
    private Integer containerStatus;
}
