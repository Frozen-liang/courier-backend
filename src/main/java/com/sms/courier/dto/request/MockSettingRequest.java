package com.sms.courier.dto.request;

import java.util.Map;
import javax.validation.constraints.NotBlank;
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
    private Map<String, String> envVariable;

}
