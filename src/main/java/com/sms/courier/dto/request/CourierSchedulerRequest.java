package com.sms.courier.dto.request;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierSchedulerRequest {

    private String imageName;
    private String containerName;
    private String version;
    private Map<String, String> envVariable;

}
