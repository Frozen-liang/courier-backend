package com.sms.courier.dto.request;

import com.sms.courier.docker.entity.PortMapping;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockSettingRequest {

    @NotBlank(message = "The id must not be empty!")
    private String id;
    private String mockUrl;
    private String imageName;
    private String containerName;
    private String version;
    @NotNull(message = "The portMappings must not be null!")
    private List<PortMapping> portMappings;
    private Map<String, String> envVariable;

}
