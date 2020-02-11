package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.model.MultiPart;
import com.sms.satp.engine.service.ApiExecuteService;
import com.sms.satp.parser.common.HttpMethod;
import com.sms.satp.repository.ProjectRepository;
import com.sms.satp.service.ApiInterfaceService;
import com.sms.satp.ApplicationTests;
import com.sms.satp.service.ProjectEnvironmentService;
import com.sms.satp.service.StatusCodeDocService;
import com.sms.satp.service.WikiService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = ApplicationTests.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Tests for DataControllerTest")
class DataControllerTest {

    @LocalServerPort
    private int port;

    @TempDir
    static Path sharedTempDir;

    @Autowired
    private ApiExecuteService apiExecuteService;

    @MockBean
    ApiInterfaceService apiInterfaceService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    ProjectEnvironmentService projectEnvironmentService;

    @MockBean
    StatusCodeDocService statusCodeDocService;

    @MockBean
    WikiService wikiService;

    private String baseUrl;
    private Path tempFile;
    private static final String TYPE = "SWAGGER";
    private static final String PROJECT_ID = "25";

    @BeforeEach
    void before() throws IOException {
        baseUrl = "http://localhost:" + port;
        tempFile = sharedTempDir.resolve("number.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(tempFile, lines);
    }

    @Test
    @DisplayName("Import the interface through the file")
    void file_upload(){
        MultiPart multiPart = MultiPart.builder().controlName("file").mimeType("mime-type1")
            .file(tempFile.toFile()).build();
        Map<String, String> param = new HashMap<>();
        param.put("type", TYPE);
        param.put("projectId", PROJECT_ID);
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().formParams(param).serverAddress(baseUrl).multiParts(Collections.singletonList(multiPart)).
                path(Constants.DATA_PATH).enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }


}