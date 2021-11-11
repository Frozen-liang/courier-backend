package com.sms.courier.docker.entity;

import com.sms.courier.docker.enmu.LabelType;
import java.util.List;
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
    private List<PortMapping> portMappings;
    private LabelType labelType;
    private Map<String, String> envVariable;
}
