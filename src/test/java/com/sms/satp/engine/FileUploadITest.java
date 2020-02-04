package com.sms.satp.engine;

import com.sms.satp.engine.model.ApiUnitRequest;
import com.sms.satp.engine.model.ApiUnitResponse;
import com.sms.satp.engine.model.MultiPart;
import com.sms.satp.engine.service.ApiExecuteService;
import com.sms.satp.parser.common.HttpMethod;
import io.restassured.http.ContentType;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(classes = ApiExecuteApplicationTests.class,

    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class FileUploadITest {

    @LocalServerPort
    private int port;

    @TempDir
    static Path sharedTempDir;

    private Path tempFile;
    private Path tempFile2;

    @Autowired
    private ApiExecuteService apiExecuteService;

    private String baseUrl;

    @BeforeEach
    public void before() throws IOException {
        baseUrl = "http://localhost:" + port;
        tempFile = sharedTempDir.resolve("number.txt");
        tempFile2 = sharedTempDir.resolve("number2.txt");
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(tempFile, lines);
        Files.write(tempFile2, lines);
    }

    @Test
    public void file_uploading_works() {

        MultiPart multiPart = MultiPart.builder().file(tempFile.toFile()).build();
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).multiParts(Collections.singletonList(multiPart)).
                path("/fileUpload").enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void file_uploading_works_with_mime_type() {
        MultiPart multiPart = MultiPart.builder().controlName("controlName").mimeType("mime-type")
            .file(tempFile.toFile()).build();
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).multiParts(Collections.singletonList(multiPart)).
                path("/fileUpload2").enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }

    @Test
    public void multiple_uploads_works() {
        MultiPart multiPart1 = MultiPart.builder().controlName("controlName1").mimeType("mime-type1")
            .file(tempFile.toFile()).build();
        MultiPart multiPart2 = MultiPart.builder().controlName("controlName2").mimeType("mime-type2")
            .file(tempFile.toFile()).build();
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().serverAddress(baseUrl).multiParts(Arrays.asList(multiPart1, multiPart2)).
                path("/multiFileUpload").enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }
    @Test
    public void file_upload_and_param_mixing_works(){
        MultiPart multiPart = MultiPart.builder().controlName("controlName").mimeType("mime-type1")
            .file(tempFile.toFile()).build();
        Map<String, String> param = new HashMap<>();
        param.put("param", "Johan");
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().formParams(param).serverAddress(baseUrl).multiParts(Collections.singletonList(multiPart)).
                path("/fileUploadWithParam").enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }
    @Test
    public void ile_uploading_works_body()  {
      Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", ContentType.BINARY);
        ApiUnitRequest apiUnitRequest =
            ApiUnitRequest.builder().headers(headers).serverAddress(baseUrl).body(tempFile.toFile()).
                path("/nonMultipartFileUpload").enableExecutionTime(true).httpMethod(HttpMethod.POST)
                .build();
        ApiUnitResponse response = apiExecuteService.execute(apiUnitRequest);
        Assertions.assertNotNull(response);
    }



}
