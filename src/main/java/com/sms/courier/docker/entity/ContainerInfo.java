package com.sms.courier.docker.entity;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerInfo {

    /**
     * Message destination.
     */
    private String destination;
    private String imageName;
    private String containerName;
    private String version;
    private Map<String, String> envVariable;
}
