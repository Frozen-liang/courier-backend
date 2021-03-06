package com.sms.courier.dto.request;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineSettingRequest {

    @NotBlank(message = "The id must not be empty!")
    private String id;
    private String version;
    @NotBlank(message = "The imageName must not be empty!")
    private String imageName;
    @Range(min = -1, message = "The taskSizeLimit must greater -1!")
    private Integer taskSizeLimit;
    private Map<String, String> envVariable;
}
