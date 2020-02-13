package com.sms.satp.service;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"logging.level.com.sms.satp = debug"})
@DisplayName("Test the service layer interface of the ProjectEnvironment")
class ProjectEnvironmentServiceDebuggerTest extends ProjectEnvironmentServiceTest {

}