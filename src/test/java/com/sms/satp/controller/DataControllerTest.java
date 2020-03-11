package com.sms.satp.controller;

import static com.sms.satp.utils.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.DataImportDto;
import com.sms.satp.service.ApiInterfaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = DataController.class)
@DisplayName("Tests for DataControllerTest")
class DataControllerTest {

    @MockBean
    ApiInterfaceService apiInterfaceService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PARAM_INVALIDATE_CODE = "400";
    private static final String TYPE = "SWAGGER";
    private static final String PROJECT_ID = "25";
    private final static String URL = "https://meshdev.smsassist.com/filestorage/swagger/v1/swagger.json";

    @Test
    @DisplayName("Import the interface through the file")
    void file_upload() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
            "test.txt", "text/plain", "Spring Framework".getBytes());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart(Constants.DATA_PATH + "/file")
            .file(mockMultipartFile)
            .param("projectId", PROJECT_ID)
            .param("type", TYPE);
        ResultActions perform = mockMvc.perform(request);
        doNothing().when(apiInterfaceService).importByFile(mockMultipartFile, TYPE, PROJECT_ID);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath(
                "$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Import the interface through the URL")
    void importByUrl() throws Exception {
        DataImportDto dataImportDto = DataImportDto.builder()
            .url(URL).type(TYPE).projectId(PROJECT_ID).build();
        doNothing().when(apiInterfaceService).importByUrl(dataImportDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.DATA_PATH + "/url")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, dataImportDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(Response.ok().build().getCode())))
            .andExpect(jsonPath("$.message", is(Response.ok().build().getMessage())));
    }

    @Test
    @DisplayName("Import the interface through the URL with url empty")
    void importByUrl_withUrlEmpty() throws Exception {
        DataImportDto dataImportDto = DataImportDto.builder()
            .type(TYPE).projectId(PROJECT_ID).build();
        doNothing().when(apiInterfaceService).importByUrl(dataImportDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.DATA_PATH + "/url")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, dataImportDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("Import url cannot be empty")));
    }

    @Test
    @DisplayName("Import the interface through the URL with projectId empty")
    void importByUrl_withpRrojectIdEmpty() throws Exception {
        DataImportDto dataImportDto = DataImportDto.builder()
            .type(TYPE).url(URL).build();
        doNothing().when(apiInterfaceService).importByUrl(dataImportDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.DATA_PATH + "/url")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, dataImportDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("ProjectId cannot be empty")));
    }

    @Test
    @DisplayName("Import the interface through the URL with type empty")
    void importByUrl_withTypeEmpty() throws Exception {
        DataImportDto dataImportDto = DataImportDto.builder()
            .url(URL).projectId(PROJECT_ID).build();
        doNothing().when(apiInterfaceService).importByUrl(dataImportDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post(Constants.DATA_PATH + "/url")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(objectMapper, dataImportDto));
        ResultActions perform = mockMvc.perform(request);
        perform.andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(PARAM_INVALIDATE_CODE)))
            .andExpect(jsonPath("$.message", is("Data type cannot be empty")));
    }
}