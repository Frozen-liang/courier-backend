package com.sms.courier.docker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortMapping {
    private int bindPort;
    private int exposedPort;
}
